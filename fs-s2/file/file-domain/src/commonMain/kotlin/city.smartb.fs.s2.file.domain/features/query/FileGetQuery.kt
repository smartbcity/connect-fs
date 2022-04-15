package city.smartb.fs.s2.file.domain.features.query

import city.smartb.fs.s2.file.domain.model.Base64String
import city.smartb.fs.s2.file.domain.model.File
import city.smartb.fs.s2.file.domain.model.FilePath
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias FileGetFunction = F2Function<FileGetQuery, FileGetResult>

typealias FileGetQuery = FilePath

@Serializable
@SerialName("FileGetResult")
data class FileGetResult(
    val file: File?,
    val content: Base64String?
)
