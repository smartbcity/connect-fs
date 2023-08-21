package city.smartb.fs.s2.file.app.service

import city.smartb.fs.api.config.S3BucketProvider
import city.smartb.fs.s2.file.app.model.Policy
import city.smartb.fs.s2.file.app.utils.parseJsonTo
import city.smartb.fs.s2.file.app.utils.toJson
import io.minio.CopyObjectArgs
import io.minio.CopySource
import io.minio.Directive
import io.minio.GetBucketPolicyArgs
import io.minio.GetObjectArgs
import io.minio.GetObjectResponse
import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.Result
import io.minio.SetBucketPolicyArgs
import io.minio.StatObjectArgs
import io.minio.StatObjectResponse
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.springframework.stereotype.Service
import s2.spring.utils.logger.Logger
import java.net.URLConnection

@Service
class S3Service(
    private val minioClient: MinioClient,
    private val s3BucketProvider: S3BucketProvider
) {

    private val logger by Logger()

    suspend  fun putObject(path: String, content: ByteArray, metadata: Map<String, String>) {
        val contentType = metadata.entries.firstOrNull { (key) -> key.lowercase() == "content-type" }
            ?.value
            ?: URLConnection.guessContentTypeFromName(path)

        PutObjectArgs.builder()
            .bucket(s3BucketProvider.getBucket())
            .`object`(path)
            .stream(content.inputStream(), content.size.toLong(), -1)
            .userMetadata(metadata)
            .apply { contentType?.let(this::contentType) }
            .build()
            .let(minioClient::putObject)
    }

    suspend  fun removeObject(path: String) {
        RemoveObjectArgs.builder()
            .bucket(s3BucketProvider.getBucket())
            .`object`(path)
            .build()
            .let(minioClient::removeObject)
    }

    suspend fun copyObject(path: String, metadata: Map<String, String>) {
        val source = CopySource.builder()
            .bucket(s3BucketProvider.getBucket())
            .`object`(path)
            .build()

        CopyObjectArgs.builder()
            .bucket(s3BucketProvider.getBucket())
            .source(source)
            .`object`(path)
            .metadataDirective(Directive.REPLACE)
            .userMetadata(metadata)
            .build()
            .let(minioClient::copyObject)
    }

    suspend fun listObjects(prefix: String): Iterable<Result<Item>> {
        return ListObjectsArgs.builder()
            .bucket(s3BucketProvider.getBucket())
            .prefix(prefix)
            .recursive(true)
            .includeUserMetadata(true)
            .build()
            .let(minioClient::listObjects)
    }

    suspend fun getObjectMetadata(path: String): Map<String, String>? {
        logger.debug("Getting metadata for $path")
        return statObject(path)
            ?.userMetadata()
            ?.mapKeys { (key) -> key.lowercase().removePrefix("x-amz-meta-") }
            .also { logger.debug("Got metadata for $path: $it") }
    }

    suspend fun statObject(path: String): StatObjectResponse? {
        return try {
            StatObjectArgs.builder()
                .bucket(s3BucketProvider.getBucket())
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

    suspend fun getObject(path: String): GetObjectResponse? {
        return try {
            GetObjectArgs.builder()
                .bucket(s3BucketProvider.getBucket())
                .`object`(path)
                .build()
                .let(minioClient::getObject)
        } catch (e: ErrorResponseException) {
            if (e.errorResponse().code() == "NoSuchKey") {
                println("NoSuchKey")
                null
            } else {
                throw e
            }
        }
    }

    suspend fun getBucketPolicy(): Policy? {
        return GetBucketPolicyArgs.builder()
            .bucket(s3BucketProvider.getBucket())
            .build()
            .let(minioClient::getBucketPolicy)
            .ifBlank { null }
            ?.parseJsonTo(Policy::class.java)
    }

    suspend fun setBucketPolicy(policy: Policy) {
        SetBucketPolicyArgs.builder()
            .bucket(s3BucketProvider.getBucket())
            .config(policy.toJson())
            .build()
            .let(minioClient::setBucketPolicy)
    }
}
