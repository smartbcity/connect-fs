package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2InitCommand

typealias FileInitFunction = F2Function<FileInitCommand, FileInitiatedEvent>

@Serializable
data class FileInitCommand(
	val id: FileId,
	val name: String,
	val objectId: String,
	val category: String?,
	val path: String,
	val hash: String,
	val metadata: Map<String, String>
): S2InitCommand

@Serializable
@SerialName("FileInitiatedEvent")
data class FileInitiatedEvent(
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
