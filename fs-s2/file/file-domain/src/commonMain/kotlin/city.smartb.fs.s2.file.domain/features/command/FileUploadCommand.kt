package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.model.Base64String
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @d2 section
 * @parent [city.smartb.fs.s2.file.domain.D2FileCommand]
 */
typealias FileUploadFunction = F2Function<FileUploadCommand, FileUploadedEvent>

/**
 * @d2 command
 * @parent [FileUploadFunction]
 */
@Serializable
data class FileUploadCommand(
	/**
	 * The FilePath linked to the file
	 * @example [city.smartb.fs.s2.file.domain.model.File.path]
	 */
	val path: FilePath,

	/**
	 * The file metadata
	 * @example [city.smartb.fs.s2.file.domain.model.File.metadata]
	 */
	val metadata: Map<String, String>,

	/**
	 * @example "Q291Y291IGplIHNhcHBlbCBjw6lkcmlr"
	 */
	val content: Base64String
)

/**
 * Result of the file upload command.
 * @d2 event
 * @parent [FileUploadFunction]
 */
@Serializable
@SerialName("FileUploadedEvent")
data class FileUploadedEvent(
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
	 * The file upload date
	 *
	 * @example 1650356237000
	 */
	val time: Long
)
