package city.smartb.registry.program.f2.chat.domain.model

import kotlin.js.JsExport
import kotlin.js.JsName
import kotlinx.serialization.Serializable

@JsExport
@JsName("ChatMetadataDTO")
interface FileAskMetadataDTO {
    val targetedFiles: List<String>
}

@Serializable
data class FileAskMetadata(
    override val targetedFiles: List<String>
): FileAskMetadataDTO
