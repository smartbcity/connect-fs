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
typealias FileDeleteFunction = F2Function<FileDeleteCommand, FileDeletedEvent>

typealias FileDeleteCommand = FilePath

/**
 * @d2 command
 * @parent [FileDeleteFunction]
 */
@Serializable
data class FileDeleteByIdCommand(
	override val id: FileId
): S2Command<FileId>

/**
 * Result of the file delete command.
 * @d2 event
 * @parent [FileDeleteFunction]
 */
@Serializable
@SerialName("FileDeletedEvent")
data class FileDeletedEvent(
	/**
	 * @example "7d895444-60d1-4df7-b112-8ecc973cb886"
	 */
	val id: FileId,
	val path: FilePath
): FileEvent {
	override fun s2Id() = id
}
