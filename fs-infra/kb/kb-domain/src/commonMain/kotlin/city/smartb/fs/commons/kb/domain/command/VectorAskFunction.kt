package city.smartb.fs.commons.kb.domain.command

import city.smartb.fs.s2.file.domain.model.FilePathDTO
import f2.dsl.cqrs.Event
import f2.dsl.fnc.F2Function
import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Ask question  on a file.
 * @d2 function
 * @parent [city.smartb.fs.commons.kb.domain.D2VectorF2Page]
 * @order 20
 */
typealias VectorAskFunction = F2Function<VectorAskQueryDTOBase, Unit>

/**
 * @d2 command
 * @parent [VectorAskFunction]
 */
@JsExport
@JsName("VectorAskQueryDTO")
interface VectorAskQueryDTO {
    val question: String
    val messages: VectorAskMessageDTO
    val metadata: Map<String, String>
//    mapOf(
//    "question" to question,
//    "messages" to history.map { message -> mapOf(
//        "content" to message.content,
//        "type" to message.type,
//        "additional_kwargs" to emptyMap<String, String>()
//        ) },
//    "metadata" to mapOf(
//    if(metadata.targetedFiles.isNotEmpty()) {
//        "targeted_files" to metadata.targetedFiles
//    }
//    else {
//        "" to ""
//    }
//    )
}

interface VectorAskMessageDTO {
    val content: String
    val type: String
    @Suppress("VariableNaming")
    val additional_kwargs: Map<String, String>
}

@Serializable
class VectorAskMessageDTOBase(
    override val content: String,
    override val type: String,
    override val additional_kwargs: Map<String, String>
): VectorAskMessageDTO

/**
 * @d2 inherit
 */
@Serializable
data class VectorAskQueryDTOBase(
    override val question: String,
    override val messages: VectorAskMessageDTOBase,
    override val metadata: Map<String, String>
): VectorAskQueryDTO

/**
 * @d2 event
 * @parent [VectorAskFunction]
 */
@JsExport
@JsName("VectorAskedEventDTO")
interface VectorAskedEventDTO: Event

/**
 * @d2 inherit
 */
@Serializable
class VectorAskedEventDTOBase: VectorAskedEventDTO
