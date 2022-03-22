package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2Command

typealias FileLogFunction = F2Function<FileLogCommand, FileLoggedEvent>

@Serializable
data class FileLogCommand(
	override val id: FileId,
	val path: String,
	val hash: String,
	val metadata: Map<String, String>
): S2Command<FileId>

@Serializable
@SerialName("FileLoggedEvent")
data class FileLoggedEvent(
	val id: FileId,
	val name: String,
	val objectId: String,
	val category: String?,
	val path: String,
	val hash: String,
	val metadata: Map<String, String>,
	val time: Long
): FileEvent {
	override fun s2Id(): FileId = id
}
