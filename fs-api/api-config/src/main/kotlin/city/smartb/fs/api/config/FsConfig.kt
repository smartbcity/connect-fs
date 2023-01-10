package city.smartb.fs.api.config

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties

@Configuration
@EnableConfigurationProperties(FsProperties::class)
class FsConfig {

    @Bean
    fun s3Properties(fsProperties: FsProperties): S3Properties = fsProperties.s3

    @Bean
    fun s3BucketProvider(fsProperties: FsProperties): S3BucketProvider {
        return S3BucketProvider(fsProperties)
    }

    @Bean
    fun minioClient(fsProperties: FsProperties): MinioClient = MinioClient.builder()
        .endpoint(fsProperties.s3.internalUrl)
        .region(fsProperties.s3.region)
        .credentials(fsProperties.s3.username, fsProperties.s3.password)
        .build()
        .apply {
            if (fsProperties.s3.dns) { enableVirtualStyleEndpoint() }
            else { disableVirtualStyleEndpoint() }
        }
}
