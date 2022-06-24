package city.smartb.fs.s2.file.domain.features.query

import city.smartb.fs.s2.file.domain.model.Base64String
import city.smartb.fs.s2.file.domain.model.File
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get a file descriptor and content from a given path.
 * @d2 function
 * @parent [city.smartb.fs.s2.file.domain.D2FilePage]
 * @order 10
 */
typealias FileGetFunction = F2Function<FileGetQuery, FileGetResult>

/**
 * @d2 query
 * @parent [FileGetFunction]
 */
typealias FileGetQuery = FilePath

/**
 * @d2 result
 * @parent [FileGetFunction]
 */
@Serializable
@SerialName("FileGetResult")
data class FileGetResult(
    /**
     * Descriptor of the file at the given path, or null if it doesn't exist.
     */
    val file: File?,

    /**
     * Content as Base64 of the file at the given path, or null if it doesn't exist.
     * @example "Q291Y291IGplIHNhcHBlbCBjw6lkcmlr"
     */
    val content: Base64String?
)
