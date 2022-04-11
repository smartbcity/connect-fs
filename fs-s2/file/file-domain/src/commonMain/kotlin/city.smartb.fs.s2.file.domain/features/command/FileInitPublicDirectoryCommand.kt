package city.smartb.fs.s2.file.domain.features.command

import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias FileInitPublicDirectoryFunction = F2Function<FileInitPublicDirectoryCommand, FilePublicDirectoryInitializedEvent>

@Serializable
data class FileInitPublicDirectoryCommand(
	val objectId: String,
	val category: String?
)

@Serializable
@SerialName("FilePublicDirectoryInitializedEvent")
data class FilePublicDirectoryInitializedEvent(
	val path: String
)
