package city.smartb.fs.s2.file.app.model

import city.smartb.fs.s2.file.domain.model.File
import io.minio.messages.Item

fun Item.toFile(buildFullPath: (String) -> String): File {
    val metadata = sanitizedMetadata()
    val path = objectName()
    val (objectId, category, name) = FilePathUtils.parseRelativePath(path)

    return File(
        id = metadata[File::id.name]!!,
        name = name,
        objectId = objectId,
        category = category,
        path = buildFullPath(path),
        metadata = metadata
    )
}

fun Item.sanitizedMetadata() = userMetadata().mapKeys { (key) -> key.lowercase().removePrefix("x-amz-meta-") }
