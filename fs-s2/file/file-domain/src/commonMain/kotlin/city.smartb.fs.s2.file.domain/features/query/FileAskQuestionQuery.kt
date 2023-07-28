package city.smartb.fs.s2.file.domain.features.query

import city.smartb.registry.program.f2.chat.domain.model.ChatMessage
import city.smartb.registry.program.f2.chat.domain.model.ChatMessageDTO
import f2.dsl.fnc.F2Function
import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.js.JsName
import city.smartb.registry.program.f2.chat.domain.model.ChatMetadata
import city.smartb.registry.program.f2.chat.domain.model.ChatMetadataDTO

/**
 * Ask a question to files.
 * @d2 function
 * @parent [city.smartb.fs.s2.file.domain.D2FilePage]
 * @order 10
 */
typealias ChatAskQuestionFunction = F2Function<ChatAskQuestionQuery, ChatAskQuestionResult>

/**
 * @d2 query
 * @parent [ChatAskQuestionFunction]
 */
@JsExport
@JsName("ChatAskQuestionQueryDTO")
interface ChatAskQuestionQueryDTO {
    /**
     * Question to ask to files.
     * @example "Where does the project take place?"
     */
    val question: String

    /**
     * Previous questions and answers in the context of the chat.
     * @example [[{
     *  "content": "What is the goal of the project?",
     *  "type": "HUMAN"
     * }, {
     *  "content": "Banana4All is an innovative initiative that aims to revolutionize the banana industry by focusing on making it more
     *  efficient and reducing its ecological impact. The project combines cutting-edge technologies, sustainable practices, and community
     *  engagement to create a holistic approach towards a greener and more sustainable banana industry.",
     *  "type": "AI"
     * }]]
     */
    val history: List<ChatMessageDTO>

    /**
     * Optional filter to restrain the knowledge search on specific files.
     */
    val metadata: ChatMetadataDTO
}

/**
 * @d2 inherit
 */
@Serializable
data class ChatAskQuestionQuery(
    override val question: String,
    override val history: List<ChatMessage>,
    override val metadata: ChatMetadata,
): ChatAskQuestionQueryDTO

/**
 * @d2 event
 * @parent [ChatAskQuestionFunction]
 */
@JsExport
@JsName("ChatAskQuestionResultDTO")
interface ChatAskQuestionResultDTO {
    /**
     * Generated response to the given question.
     * @example "The Banana4All project is primarily focused on implementing its initiatives in the banana-growing regions of Latin America,
     * specifically in countries such as Ecuador, Costa Rica, Colombia, Guatemala, and Honduras. These countries have a significant presence
     * in the global banana market and are known for their large-scale banana plantations.\n
     * By targeting these regions, Banana4All aims to address the ecological impact of banana production in areas where it is most prevalent.
     * These countries have a high concentration of banana farms and play a crucial role in supplying bananas to international markets.\n
     * However, it is important to note that while the project's initial focus may be on Latin America, its principles and strategies can be
     * adapted and implemented in other banana-growing regions worldwide. The ultimate goal of Banana4All is to bring about positive change
     * and sustainability to the banana industry on a global scale."
     */
    val item: String
}

/**
 * @d2 inherit
 */
@Serializable
data class ChatAskQuestionResult(
    override val item: String
): ChatAskQuestionResultDTO
