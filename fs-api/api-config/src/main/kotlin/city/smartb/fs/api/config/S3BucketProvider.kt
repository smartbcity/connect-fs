package city.smartb.fs.api.config

import city.smartb.fs.api.error.NoBucketConfiguredError
import city.smartb.i2.spring.boot.auth.AuthenticationProvider
import f2.dsl.cqrs.error.asException

class S3BucketProvider(
    private val fsProperties: FsProperties
) {

    suspend fun getBucket(): String {
        return getSpace() ?: deprecatedBucket() ?: throw NoBucketConfiguredError().asException()
    }

    private suspend fun getSpace(): String? {
        return fsProperties.space?.name ?: fsProperties.space?.jwt?.claim?.let { claim ->
            AuthenticationProvider.getPrincipal()?.getClaim<String>(claim)
        }
    }

    private fun deprecatedBucket(): String? {
        return fsProperties.s3.bucket
    }

}
