package city.smartb.fs.s2.file.app.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {

    @Value("\${fs.s3.internal-url}")
    lateinit var internalUrl: String

    @Value("\${fs.s3.external-url}")
    lateinit var externalUrl: String

    @Value("\${fs.s3.region}")
    lateinit var region: String

    @Value("\${fs.s3.username}")
    lateinit var username: String

    @Value("\${fs.s3.password}")
    lateinit var password: String

    @Value("\${fs.s3.bucket}")
    lateinit var bucket: String

    @Value("\${fs.s3.dns}")
    var dns: Boolean = false

    @Bean
    fun minioClient(): MinioClient = MinioClient.builder()
        .endpoint(internalUrl)
        .region(region)
        .credentials(username, password)
        .build()
        .apply {
            if (dns) { enableVirtualStyleEndpoint() }
            else { disableVirtualStyleEndpoint() }
        }
}
