package city.smartb.fs.s2.file.app.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {

    @Value("\${s3.url}")
    lateinit var url: String

    @Value("\${s3.region}")
    lateinit var region: String

    @Value("\${s3.username}")
    lateinit var username: String

    @Value("\${s3.password}")
    lateinit var password: String

    @Value("\${s3.bucket}")
    lateinit var bucket: String

    @Value("\${s3.dns}")
    var dns: Boolean = false

    @Bean
    fun minioClient(): MinioClient = MinioClient.builder()
        .endpoint(url)
        .region(region)
        .credentials(username, password)
        .build()
        .apply {
            if (dns) { enableVirtualStyleEndpoint() }
            else { disableVirtualStyleEndpoint() }
        }
}
