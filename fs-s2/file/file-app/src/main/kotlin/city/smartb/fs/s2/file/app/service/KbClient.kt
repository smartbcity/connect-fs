package city.smartb.fs.s2.file.app.service

import city.smartb.fs.s2.file.client.Client
import city.smartb.fs.s2.file.domain.model.FilePath
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import s2.spring.utils.logger.Logger

class KbClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {}
): Client(baseUrl, block) {
    private val logger by Logger()
    suspend fun fileVectorize(path: FilePath, file: ByteArray, metadata: Map<String, String>) {
        logger.debug("Vectorizing file $path")
        postFormData<Unit>("") {
            file("file", file, path.name)
            param("metadata", metadata + ("fsPath" to path.toString()))
        }
        logger.debug("File $path vectorized")
    }
}

@Configuration
@ConditionalOnProperty("fs.kb.url")
class KbConfiguration {
    @Value("\${fs.kb.url}")
    lateinit var vectorpediaUrl: String

    @Bean
    fun vectorpediaClient() = KbClient(vectorpediaUrl) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }
    }
}
