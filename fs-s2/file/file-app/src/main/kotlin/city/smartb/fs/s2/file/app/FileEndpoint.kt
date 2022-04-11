package city.smartb.fs.s2.file.app

import city.smartb.fs.s2.file.app.config.FsSsmConfig
import city.smartb.fs.s2.file.app.config.S3Config
import city.smartb.fs.s2.file.app.model.FilePathUtils
import city.smartb.fs.s2.file.app.model.toFile
import city.smartb.fs.s2.file.app.model.toFileUploadedEvent
import city.smartb.fs.s2.file.app.service.S3Service
import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.features.command.FileDeleteByIdCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeleteFunction
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadFunction
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import city.smartb.fs.s2.file.domain.features.query.FileGetFunction
import city.smartb.fs.s2.file.domain.features.query.FileGetListFunction
import city.smartb.fs.s2.file.domain.features.query.FileGetListResult
import city.smartb.fs.s2.file.domain.features.query.FileGetResult
import city.smartb.fs.s2.file.domain.model.File
import f2.dsl.fnc.f2Function
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

@Configuration
class FileEndpoint(
    private val fileDeciderSourcingImpl: FileDeciderSourcingImpl,
    private val fsSsmConfig: FsSsmConfig,
    private val s3Config: S3Config,
    private val s3Service: S3Service,
) {

    @Bean
    fun getFile(): FileGetFunction = f2Function { query ->
        val path = FilePathUtils.buildRelativePath(
            objectId = query.objectId,
            category = query.category,
            name = query.name
        )
        val metadata = s3Service.getObjectMetadata(path)
            ?: return@f2Function FileGetResult(null, null)

        val content = s3Service.getObject(path)!!.use { input ->
            input.readBytes().encodeToB64()
        }

        FileGetResult(
            file = File(
                id = metadata["id"].orEmpty(),
                name = query.name,
                objectId = query.objectId,
                category = query.category,
                url = buildUrl(path),
                metadata = metadata
            ),
            content = content
        )
    }

    @Bean
    fun listFiles(): FileGetListFunction = f2Function { query ->
        val prefix = FilePathUtils.buildRelativePath(
            objectId = query.objectId,
            category = query.category,
            name = ""
        )
        s3Service.listObjects(prefix)
            .map { obj -> obj.get().toFile(::buildUrl) }
            .let(::FileGetListResult)
    }

    @Bean
    fun uploadFile(): FileUploadFunction = f2Function { cmd ->
        val path = FilePathUtils.buildRelativePath(
            objectId = cmd.objectId,
            category = cmd.category,
            name = cmd.name
        )

        val fileMetadata = s3Service.getObjectMetadata(path)
        val fileExists = fileMetadata != null
        val fileId = fileMetadata?.get("id") ?: UUID.randomUUID().toString()

        val fileByteArray = cmd.content.decodeB64()
        s3Service.putObject(
            path = path,
            content = fileByteArray,
            metadata = cmd.metadata.plus("id" to fileId)
        )

        if (mustBeSavedToSsm(cmd.category)) {
            if (fileExists) {
                fileByteArray.logFileInSsm(cmd, fileId, buildUrl(path))
            } else {
                fileByteArray.initFileInSsm(cmd, fileId, buildUrl(path))
            }
        } else {
            FileUploadedEvent(
                id = fileId,
                name = cmd.name,
                objectId = cmd.objectId,
                category = cmd.category,
                url = buildUrl(path),
                hash = fileByteArray.hash(),
                metadata = cmd.metadata,
                time = System.currentTimeMillis()
            )
        }
    }

    @Bean
    fun deleteFile(): FileDeleteFunction = f2Function { cmd ->
        val path = FilePathUtils.buildRelativePath(
            objectId = cmd.objectId,
            category = cmd.category,
            name = cmd.name
        )

        val metadata = s3Service.getObjectMetadata(path)
            ?: throw IllegalArgumentException("File not found at path [$path]")

        val id = metadata["id"]!!

        s3Service.removeObject(path)
        if (mustBeSavedToSsm(cmd.category)) {
            fileDeciderSourcingImpl.delete(FileDeleteByIdCommand(id = id))
        }

        FileDeletedEvent(id = id)
    }

    private fun mustBeSavedToSsm(category: String?) = category in fsSsmConfig.categories.orEmpty()

    private suspend fun ByteArray.initFileInSsm(cmd: FileUploadCommand, fileId: FileId, path: String): FileUploadedEvent {
        return FileInitCommand(
            id = fileId,
            name = cmd.name,
            objectId = cmd.objectId,
            category = cmd.category,
            path = path,
            hash = hash(),
            metadata = cmd.metadata,
        ).let { fileDeciderSourcingImpl.init(it).toFileUploadedEvent() }
    }

    private suspend fun ByteArray.logFileInSsm(cmd: FileUploadCommand, fileId: FileId, path: String): FileUploadedEvent {
        return FileLogCommand(
            id = fileId,
            path = path,
            hash = hash(),
            metadata = cmd.metadata,
        ).let { fileDeciderSourcingImpl.log(it).toFileUploadedEvent() }
    }

    private fun ByteArray.hash() = MessageDigest
        .getInstance("SHA-256")
        .digest(this)
        .encodeToB64()

    private fun ByteArray.encodeToB64() = Base64.getEncoder().encodeToString(this)
    private fun String.decodeB64() = Base64.getDecoder().decode(substringAfterLast(";base64,"))

    private fun buildUrl(path: String) = FilePathUtils.buildAbsolutePath(path, s3Config.externalUrl, s3Config.bucket, s3Config.dns)
}
