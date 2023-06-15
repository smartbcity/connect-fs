package city.smartb.fs.s2.file.app.service

import city.smartb.fs.s2.file.client.Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class VectorpediaClient(baseUrl: String): Client(baseUrl) {
    suspend fun fileVectorize(file: ByteArray, metadata: Map<String, String>): Unit = postFormData("") {
        file("file", file, "file.pdf")
        param("metadata", metadata)
    }
}

@Configuration
@ConditionalOnProperty("fs.vectorpedia.url")
class VectorpediaConfiguration {
    @Value("\${fs.vectorpedia.url}")
    lateinit var vectorpediaUrl: String

    @Bean
    fun vectorpediaClient() = VectorpediaClient(vectorpediaUrl)
}
