package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2Command

typealias FileDeleteFunction = F2Function<FileDeleteCommand, FileDeletedEvent>

typealias FileDeleteCommand = FilePath

@Serializable
data class FileDeleteByIdCommand(
	override val id: FileId
): S2Command<FileId>

@Serializable
@SerialName("FileDeletedEvent")
data class FileDeletedEvent(
	val id: FileId
): FileEvent {
	override fun s2Id() = id
}
