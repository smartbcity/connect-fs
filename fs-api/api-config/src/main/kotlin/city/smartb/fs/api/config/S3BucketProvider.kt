package city.smartb.fs.api.config

import city.smartb.fs.api.error.NoBucketConfiguredException
import city.smartb.i2.spring.boot.auth.AuthenticationProvider

class S3BucketProvider(
    private val fsProperties: FsProperties
) {

    suspend fun getBucket(): String {
        return getSpace() ?: deprecatedBucket() ?: throw NoBucketConfiguredException()
    }

    private suspend fun getSpace(): String? {
        return fsProperties.space?.name ?: fsProperties.space?.jwt?.claim?.let { claim ->
            AuthenticationProvider.getPrincipal()?.getClaim<String>(claim)
        }
    }

    private suspend fun deprecatedBucket(): String? {
        return fsProperties.s3.bucket
    }

}
