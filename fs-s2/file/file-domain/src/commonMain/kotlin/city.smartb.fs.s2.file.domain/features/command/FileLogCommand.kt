package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2Command

/**
 * @d2 section
 * @parent [city.smartb.fs.s2.file.domain.D2FileCommand]
 */
typealias FileLogFunction = F2Function<FileLogCommand, FileLoggedEvent>

/**
 * @d2 command
 * @parent [FileLogFunction]
 */
@Serializable
data class FileLogCommand(
    /**
     * @example "7d895444-60d1-4df7-b112-8ecc973cb886"
     */
	override val id: FileId,
	val path: String,
	val hash: String,
	val metadata: Map<String, String>
): S2Command<FileId>

/**
 * Result of the file log command.
 * @d2 event
 * @parent [FileLogFunction]
 */
@Serializable
@SerialName("FileLoggedEvent")
data class FileLoggedEvent(
    /**
     * @example "7d895444-60d1-4df7-b112-8ecc973cb886"
     */
    val id: FileId,

    val path: FilePath,

    /**
     * The file Url
     * @example [city.smartb.fs.s2.file.domain.model.File.url]
     */
    val url: String,

    /**
     * The file hash
     * @example "rPpebW/JVqIlEg/HLu4H3GXS2dB+34TZnZjz54wm2a4="
     */
    val hash: String,

    /**
     * The file metadata
     * @example [city.smartb.fs.s2.file.domain.model.File.metadata]
     */
    val metadata: Map<String, String>,

    /**
     * The file upload date
     * @example 1650356237000
     */
    val time: Long
): FileEvent {
	override fun s2Id(): FileId = id
}
