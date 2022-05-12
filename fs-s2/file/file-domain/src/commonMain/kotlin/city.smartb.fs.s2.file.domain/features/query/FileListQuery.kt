package city.smartb.fs.s2.file.domain.features.query

import city.smartb.fs.s2.file.domain.model.File
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get a list of the file descriptors at the given path.
 * @d2 section
 * @parent [city.smartb.fs.s2.file.domain.D2FileQuerySection]
 * @order 20
 */
typealias FileListFunction = F2Function<FileListCommand, FileListResult>

/**
 * @d2 query
 * @parent [FileListFunction]
 */
@Serializable
data class FileListCommand(
    /**
     * Type of object the file are attached to.
     * @example [city.smartb.fs.s2.file.domain.model.FilePath.objectType]
     */
    val objectType: String,

    /**
     * Identifier of the object the file are attached to.
     * @example [city.smartb.fs.s2.file.domain.model.FilePath.objectId]
     */
    val objectId: String,

    /**
     * Directory containing the files.
     * @example [city.smartb.fs.s2.file.domain.model.FilePath.directory]
     */
    val directory: String?
)

/**
 * @d2 event
 * @parent [FileListFunction]
 */
@Serializable
@SerialName("FileListResult")
data class FileListResult(
    /**
     * List of descriptors of the files within the given path
     */
    val items: List<File>
)
