package city.smartb.fs.s2.file.app

import city.smartb.fs.s2.file.app.config.FsSsmConfig
import city.smartb.fs.s2.file.app.config.S3Config
import city.smartb.fs.s2.file.app.entity.FileEntity
import city.smartb.fs.s2.file.app.model.toFileUploadedEvent
import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadFunction
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import f2.dsl.fnc.f2Function
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.StatObjectArgs
import io.minio.errors.ErrorResponseException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

@Configuration
class FileEndpoint(
    private val fileDeciderSourcingImpl: FileDeciderSourcingImpl,
    private val fsSsmConfig: FsSsmConfig,
    private val minioClient: MinioClient,
    private val s3Config: S3Config
) {

    @Bean
    fun uploadFile(): FileUploadFunction = f2Function { cmd ->
        val path = FileEntity.path(
            objectId = cmd.objectId,
            category = cmd.category,
            name = cmd.name
        )

        val fileMetadata = getFileMetadata(path)
        val fileExists = fileMetadata != null
        val fileId = fileMetadata?.get("id") ?: UUID.randomUUID().toString()

        val fileByteArray = cmd.content.decodeB64()
        fileByteArray.saveFileInS3(path, fileId, cmd.metadata)

        if (cmd.category in fsSsmConfig.categories) {
            if (fileExists) {
                fileByteArray.logFileInSsm(cmd, fileId, buildFullPath(path))
            } else {
                fileByteArray.initFileInSsm(cmd, fileId, buildFullPath(path))
            }
        } else {
            FileUploadedEvent(
                id = fileId,
                name = cmd.name,
                objectId = cmd.objectId,
                category = cmd.category,
                path = buildFullPath(path),
                hash = fileByteArray.hash(),
                metadata = cmd.metadata,
                time = System.currentTimeMillis()
            )
        }
    }

    private fun ByteArray.saveFileInS3(path: String, id: FileId, metadata: Map<String, String>) {
        PutObjectArgs.builder()
            .bucket(s3Config.bucket)
            .`object`(path)
            .stream(inputStream(), size.toLong(), -1)
            .userMetadata(metadata.plus("id" to id))
            .apply {
                metadata.entries.firstOrNull { (key) -> key.lowercase() == "content-type" }?.let { (_, contentType) ->
                    contentType(contentType)
                }
            }
            .build()
            .let(minioClient::putObject)
    }

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

    private fun getFileMetadata(path: String): Map<String, String>? {
        return try {
            StatObjectArgs.builder()
                .bucket(s3Config.bucket)
                .`object`(path)
                .build()
                .let(minioClient::statObject)
                .userMetadata()
                .mapKeys { (key) -> key.lowercase().removePrefix("x-amz-meta-") }
        } catch (e: ErrorResponseException) {
            if (e.errorResponse().code() == "NoSuchKey") {
                null
            } else {
                throw e
            }
        }
    }

    private fun buildFullPath(path: String) = "${s3Config.url}/${s3Config.bucket}/$path"
}
