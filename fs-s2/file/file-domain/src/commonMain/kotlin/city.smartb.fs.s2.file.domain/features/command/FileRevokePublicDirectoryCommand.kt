package city.smartb.fs.s2.file.domain.features.command

import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias FileRevokePublicDirectoryFunction = F2Function<FileRevokePublicDirectoryCommand, FilePublicDirectoryRevokedEvent>

@Serializable
data class FileRevokePublicDirectoryCommand(
	val objectType: String,
	val objectId: String,
	val directory: String
)

@Serializable
@SerialName("FilePublicDirectoryRevokedEvent")
data class FilePublicDirectoryRevokedEvent(
	val path: String
)
