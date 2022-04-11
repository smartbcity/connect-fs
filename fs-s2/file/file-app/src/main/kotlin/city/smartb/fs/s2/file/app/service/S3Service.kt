package city.smartb.fs.s2.file.app.service

import city.smartb.fs.s2.file.app.config.S3Config
import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.Result
import io.minio.StatObjectArgs
import io.minio.StatObjectResponse
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.springframework.stereotype.Service
import java.net.URLConnection

@Service
class S3Service(
    private val minioClient: MinioClient,
    private val s3Config: S3Config
) {

    fun putObject(path: String, content: ByteArray, metadata: Map<String, String>) {
        val contentType = metadata.entries.firstOrNull { (key) -> key.lowercase() == "content-type" }
            ?.value
            ?: URLConnection.guessContentTypeFromName(path)

        PutObjectArgs.builder()
            .bucket(s3Config.bucket)
            .`object`(path)
            .stream(content.inputStream(), content.size.toLong(), -1)
            .userMetadata(metadata)
            .apply { contentType?.let(this::contentType) }
            .build()
            .let(minioClient::putObject)
    }

    fun removeObject(path: String) {
        RemoveObjectArgs.builder()
            .bucket(s3Config.bucket)
            .`object`(path)
            .build()
            .let(minioClient::removeObject)
    }

    fun listObjects(prefix: String): Iterable<Result<Item>> {
        return ListObjectsArgs.builder()
            .bucket(s3Config.bucket)
            .prefix(prefix)
            .recursive(true)
            .includeUserMetadata(true)
            .build()
            .let(minioClient::listObjects)
    }

    fun getObjectMetadata(path: String) = statObject(path)
        ?.userMetadata()
        ?.mapKeys { (key) -> key.lowercase().removePrefix("x-amz-meta-") }

    fun statObject(path: String): StatObjectResponse? {
        return try {
            StatObjectArgs.builder()
                .bucket(s3Config.bucket)
                .`object`(path)
                .build()
                .let(minioClient::statObject)
        } catch (e: ErrorResponseException) {
            if (e.errorResponse().code() == "NoSuchKey") {
                null
            } else {
                throw e
            }
        }
    }
}