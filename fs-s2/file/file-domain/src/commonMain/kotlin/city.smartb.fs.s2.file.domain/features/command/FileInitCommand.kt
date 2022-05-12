package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2InitCommand

/**
 * @d2 section
 * @parent [city.smartb.fs.s2.file.domain.D2FileCommand]
 */
typealias FileInitFunction = F2Function<FileInitCommand, FileInitiatedEvent>

/**
 * @d2 command
 * @parent [FileInitFunction]
 */
@Serializable
data class FileInitCommand(
	/**
	 * @example "7d895444-60d1-4df7-b112-8ecc973cb886"
	 */
	val id: FileId,
	val path: FilePath,
	val url: String,
	val hash: String,
	val metadata: Map<String, String>
): S2InitCommand

/**
 * Result of the file initiated command.
 * @d2 event
 * @parent [FileInitFunction]
 */
@Serializable
@SerialName("FileInitiatedEvent")
data class FileInitiatedEvent(
	/**
	 * @example "7d895444-60d1-4df7-b112-8ecc973cb886"
	 */
	val id: FileId,

	val path: FilePath,

	/**
	 * @example [city.smartb.fs.s2.file.domain.model.File.url]
	 */
	val url: String,

	/**
	 * @example "rPpebW/JVqIlEg/HLu4H3GXS2dB+34TZnZjz54wm2a4="
	 */
	val hash: String,

	/**
	 * @example [city.smartb.fs.s2.file.domain.model.File.metadata]
	 */
	val metadata: Map<String, String>,

	/**
	 * The file initiation time
	 *
	 * @example 1650356237000
	 */
	val time: Long
): FileEvent {
	override fun s2Id(): FileId = id
}
