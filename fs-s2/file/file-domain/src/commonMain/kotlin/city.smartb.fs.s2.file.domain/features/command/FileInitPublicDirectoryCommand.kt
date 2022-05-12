package city.smartb.fs.s2.file.domain.features.command

import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @d2 section
 * @parent [city.smartb.fs.s2.file.domain.D2FileCommand]
 */
typealias FileInitPublicDirectoryFunction = F2Function<FileInitPublicDirectoryCommand, FilePublicDirectoryInitializedEvent>

/**
 * @d2 command
 * @parent [FileInitPublicDirectoryFunction]
 */
@Serializable
data class FileInitPublicDirectoryCommand(
	val objectType: String,
	val objectId: String,
	val directory: String
)

/**
 * Result of the file public directory initialize command.
 * @d2 event
 * @parent [FileInitPublicDirectoryFunction]
 */
@Serializable
@SerialName("FilePublicDirectoryInitializedEvent")
data class FilePublicDirectoryInitializedEvent(
	val path: String
)
