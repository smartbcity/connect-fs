package city.smartb.registry.program.f2.chat.domain.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
@JsName("ChatMessageDTO")
interface ChatMessageDTO {
    val content: String
    val type: String
}

@Serializable
data class ChatMessage(
    override val content: String,
    override val type: String
): ChatMessageDTO
