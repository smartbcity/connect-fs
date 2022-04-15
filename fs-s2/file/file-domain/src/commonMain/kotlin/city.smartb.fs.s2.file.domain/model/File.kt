package city.smartb.fs.s2.file.domain.model

import city.smartb.fs.s2.file.domain.automate.FileId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("File")
data class File(
    val id: FileId,
    val path: FilePath,
    val url: String,
    val metadata: Map<String, String>
)
