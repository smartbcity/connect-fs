package city.smartb.fs.s2.file.domain.features.query

import city.smartb.fs.s2.file.domain.model.File
import f2.dsl.fnc.F2Function
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias FileGetListFunction = F2Function<FileGetListCommand, FileGetListResult>

@Serializable
data class FileGetListCommand(
    val objectId: String,
    val category: String?
)

@Serializable
@SerialName("FileGetListResult")
data class FileGetListResult(
    val files: List<File>
)
