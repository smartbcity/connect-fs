package city.smartb.fs.s2.file.app

import city.smartb.fs.s2.file.app.config.FsSsmConfig
import city.smartb.fs.s2.file.app.config.S3Config
import city.smartb.fs.s2.file.app.model.S3Action
import city.smartb.fs.s2.file.app.model.S3Effect
import city.smartb.fs.s2.file.app.model.Statement
import city.smartb.fs.s2.file.app.model.toFile
import city.smartb.fs.s2.file.app.model.toFileUploadedEvent
import city.smartb.fs.s2.file.app.service.S3Service
import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.features.command.FileDeleteByIdCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeleteFunction
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitPublicDirectoryFunction
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryInitializedEvent
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryRevokedEvent
import city.smartb.fs.s2.file.domain.features.command.FileRevokePublicDirectoryFunction
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadFunction
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import city.smartb.fs.s2.file.domain.features.query.FileGetFunction
import city.smartb.fs.s2.file.domain.features.query.FileGetListFunction
import city.smartb.fs.s2.file.domain.features.query.FileGetListResult
import city.smartb.fs.s2.file.domain.features.query.FileGetResult
import city.smartb.fs.s2.file.domain.model.File
import city.smartb.fs.s2.file.domain.model.FilePath
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
        val path = query.toString()
        val metadata = s3Service.getObjectMetadata(path)
            ?: return@f2Function FileGetResult(null, null)

        val content = s3Service.getObject(path)!!.use { input ->
            input.readBytes().encodeToB64()
        }

        FileGetResult(
            file = File(
                id = metadata["id"].orEmpty(),
                path = FilePath(
                    objectType = query.objectType,
                    objectId = query.objectId,
                    directory = query.directory,
                    name = query.name,
                ),
                url = query.buildUrl(),
                metadata = metadata
            ),
            content = content
        )
    }

    @Bean
    fun listFiles(): FileGetListFunction = f2Function { query ->
        val prefix = FilePath(
            objectType = query.objectType,
            objectId = query.objectId,
            directory = query.directory ?: "",
            name = ""
        ).toPartialPrefix()

        s3Service.listObjects(prefix)
            .map { obj -> obj.get().toFile { it.buildUrl() } }
            .let(::FileGetListResult)
    }

    @Bean
    fun uploadFile(): FileUploadFunction = f2Function { cmd ->
        val pathStr = cmd.path.toString()

        val fileMetadata = s3Service.getObjectMetadata(pathStr)
        val fileExists = fileMetadata != null
        val fileId = fileMetadata?.get("id") ?: UUID.randomUUID().toString()

        val fileByteArray = cmd.content.decodeB64()
        s3Service.putObject(
            path = pathStr,
            content = fileByteArray,
            metadata = cmd.metadata.plus("id" to fileId)
        )

        if (mustBeSavedToSsm(cmd.path.directory)) {
            if (fileExists) {
                fileByteArray.logFileInSsm(cmd, fileId, cmd.path.buildUrl())
            } else {
                fileByteArray.initFileInSsm(cmd, fileId, cmd.path.buildUrl())
            }
        } else {
            FileUploadedEvent(
                id = fileId,
                path = cmd.path,
                url = cmd.path.buildUrl(),
                hash = fileByteArray.hash(),
                metadata = cmd.metadata,
                time = System.currentTimeMillis()
            )
        }
    }

    @Bean
    fun deleteFile(): FileDeleteFunction = f2Function { cmd ->
        val pathStr = cmd.toString()
        val metadata = s3Service.getObjectMetadata(pathStr)
            ?: throw IllegalArgumentException("File not found at path [$pathStr]")

        val id = metadata["id"]!!

        s3Service.removeObject(pathStr)
        if (mustBeSavedToSsm(cmd.directory)) {
            fileDeciderSourcingImpl.delete(FileDeleteByIdCommand(id = id))
        }

        FileDeletedEvent(id = id)
    }

    @Bean
    fun initPublicDirectory(): FileInitPublicDirectoryFunction = f2Function { cmd ->
        val path = FilePath(
            objectType = cmd.objectType,
            objectId = cmd.objectId,
            directory = cmd.directory,
            name = "*"
        ).toString()

        val policy = s3Service.getBucketPolicy()
        policy.getOrAddStatementWith(S3Effect.ALLOW, S3Action.GET_OBJECT)
            .addResource(bucket = s3Config.bucket, path = path)
        s3Service.setBucketPolicy(policy)

        FilePublicDirectoryInitializedEvent(
            path = Statement.resourcePath(bucket = s3Config.bucket, path = path)
        )
    }

    @Bean
    fun revokePublicDirectory(): FileRevokePublicDirectoryFunction = f2Function { cmd ->
        val path = FilePath(
            objectType = cmd.objectType,
            objectId = cmd.objectId,
            directory = cmd.directory,
            name = "*"
        ).toString()

        val policy = s3Service.getBucketPolicy()
        policy.getStatementWith(S3Effect.ALLOW, S3Action.GET_OBJECT)
            ?.removeResource(bucket = s3Config.bucket, path = path)
        s3Service.setBucketPolicy(policy)

        FilePublicDirectoryRevokedEvent(
            path = Statement.resourcePath(bucket = s3Config.bucket, path = path)
        )
    }

    private fun mustBeSavedToSsm(category: String?) = category in fsSsmConfig.directories.orEmpty()

    private suspend fun ByteArray.initFileInSsm(cmd: FileUploadCommand, fileId: FileId, path: String): FileUploadedEvent {
        return FileInitCommand(
            id = fileId,
            path = cmd.path,
            url = path,
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

    private fun FilePath.buildUrl() = buildUrl(s3Config.externalUrl, s3Config.bucket, s3Config.dns)
}
