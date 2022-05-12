package city.smartb.fs.s2.file.domain.features.command

import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @d2 section
 * @parent [city.smartb.fs.s2.file.domain.D2FileCommand]
 */
typealias FileRevokePublicDirectoryFunction = F2Function<FileRevokePublicDirectoryCommand, FilePublicDirectoryRevokedEvent>


/**
 * @d2 command
 * @parent [FileRevokePublicDirectoryFunction]
 */
@Serializable
data class FileRevokePublicDirectoryCommand(
	val objectType: String,
	val objectId: String,
	val directory: String
)

/**
 * Result of the file revoke public directory command.
 * @d2 event
 * @parent [FileRevokePublicDirectoryFunction]
 */
@Serializable
@SerialName("FilePublicDirectoryRevokedEvent")
data class FilePublicDirectoryRevokedEvent(
	val path: String
)
