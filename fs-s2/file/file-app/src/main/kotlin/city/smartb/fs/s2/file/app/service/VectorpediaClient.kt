package city.smartb.fs.s2.file.app.service

import city.smartb.fs.s2.file.client.Client
import city.smartb.fs.s2.file.domain.model.FilePath
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class VectorpediaClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {}
): Client(baseUrl, block) {
    suspend fun fileVectorize(path: FilePath, file: ByteArray, metadata: Map<String, String>): Unit = postFormData("") {
        file("file", file, path.name)
        param("metadata", metadata + ("fsPath" to path.toString()))
    }
}

@Configuration
@ConditionalOnProperty("fs.vectorpedia.url")
class VectorpediaConfiguration {
    @Value("\${fs.vectorpedia.url}")
    lateinit var vectorpediaUrl: String

    @Bean
    fun vectorpediaClient() = VectorpediaClient(vectorpediaUrl) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }
    }
}
