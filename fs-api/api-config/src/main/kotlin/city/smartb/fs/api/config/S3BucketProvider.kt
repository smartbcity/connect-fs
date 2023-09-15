package city.smartb.fs.api.config

import city.smartb.f2.spring.boot.auth.AuthenticationProvider
import city.smartb.fs.commons.error.NoBucketConfiguredError
import f2.dsl.cqrs.error.asException

class S3BucketProvider(
    private val fsProperties: FsProperties
) {

    suspend fun getBucket(): String {
        return getSpace() ?: deprecatedBucket() ?: throw NoBucketConfiguredError().asException()
    }

    private suspend fun getSpace(): String? {
        return fsProperties.space?.name ?: AuthenticationProvider.getSpace()
    }

    private fun deprecatedBucket(): String? {
        return fsProperties.s3.bucket
    }

}
