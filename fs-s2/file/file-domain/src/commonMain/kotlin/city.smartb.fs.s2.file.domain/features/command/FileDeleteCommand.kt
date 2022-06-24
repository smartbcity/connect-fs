package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2Command

/**
 * Delete a given file.
 * @d2 function
 * @parent [city.smartb.fs.s2.file.domain.D2FilePage]
 * @order 20
 */
typealias FileDeleteFunction = F2Function<FileDeleteCommand, FileDeletedEvent>

/**
 * @d2 command
 * @parent [FileDeleteFunction]
 */
typealias FileDeleteCommand = FilePath

@Serializable
data class FileDeleteByIdCommand(
	override val id: FileId
): S2Command<FileId>

/**
 * @d2 event
 * @parent [FileDeleteFunction]
 */
@Serializable
@SerialName("FileDeletedEvent")
data class FileDeletedEvent(
	/**
	 * Identifier of the deleted file.
	 */
	val id: FileId,

	/**
	 * Path of the deleted file.
	 */
	val path: FilePath
): FileEvent {
	override fun s2Id() = id
}
