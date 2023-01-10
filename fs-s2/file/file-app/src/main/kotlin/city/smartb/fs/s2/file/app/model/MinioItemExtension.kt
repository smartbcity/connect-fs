package city.smartb.fs.s2.file.app.model

import city.smartb.fs.s2.file.domain.model.File
import city.smartb.fs.s2.file.domain.model.FilePath
import io.minio.messages.Item

suspend fun Item.toFile(buildUrl: suspend (FilePath) -> String): File {
    val metadata = sanitizedMetadata()
    val path = FilePath.from(objectName())

    return File(
        id = metadata[File::id.name].orEmpty(),
        path = path,
        url = buildUrl(path),
        metadata = metadata
    )
}

fun Item.sanitizedMetadata() = userMetadata().orEmpty().mapKeys { (key) -> key.lowercase().removePrefix("x-amz-meta-") }
