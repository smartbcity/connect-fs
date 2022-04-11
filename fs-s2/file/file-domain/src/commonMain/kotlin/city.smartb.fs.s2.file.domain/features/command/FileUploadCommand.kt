package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.model.Base64String
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias FileUploadFunction = F2Function<FileUploadCommand, FileUploadedEvent>

@Serializable
data class FileUploadCommand(
	val name: String,
	val objectId: String,
	val category: String?,
	val metadata: Map<String, String>,
	val content: Base64String
)

@Serializable
@SerialName("FileUploadedEvent")
data class FileUploadedEvent(
	val id: FileId,
	val name: String,
	val objectId: String,
	val category: String?,
	val url: String,
	val hash: String,
	val metadata: Map<String, String>,
	val time: Long
)
