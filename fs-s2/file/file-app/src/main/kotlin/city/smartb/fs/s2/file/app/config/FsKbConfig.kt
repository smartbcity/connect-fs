package city.smartb.fs.s2.file.app.config

import city.smartb.fs.s2.file.app.service.KbClient
import io.ktor.client.plugins.HttpTimeout
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty("fs.kb.url")
@EnableConfigurationProperties(FsKbConfig::class)
@Configuration
class FsKbConfiguration {
    @Bean
    fun kbClient(fsKbConfig: FsKbConfig) = KbClient(fsKbConfig.url) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }
    }
}

@ConfigurationProperties(prefix = "fs.kb")
class FsKbConfig(
    val url: String,
)
