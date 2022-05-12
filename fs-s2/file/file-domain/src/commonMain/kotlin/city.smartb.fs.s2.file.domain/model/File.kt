package city.smartb.fs.s2.file.domain.model

import city.smartb.fs.s2.file.domain.automate.FileId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @d2 model
 * @title Model
 * @parent [city.smartb.fs.s2.file.domain.D2FileModelSection]
 * @visual json
 */
@Serializable
@SerialName("File")
data class File(
    /**
     * The file id
     * @example "91541047-5da8-4161-af79-3fd367fc014e"
     */
    val id: FileId,

    /**
     * The FilePath linked to the file
     */
    val path: FilePath,

    /**
     * The file Url
     * @example "https://s3.myproject.smart-b.io/myBucket/myObjectType/ObjectId/mediaDirectory/FileName.jpg"
     */
    val url: String,

    /**
     * The file metadata
     * @example {
     *  "Pragma" : "no-cache",
     *  "Content-Type" : "image/jpeg"
     *  }
     */
    val metadata: Map<String, String>
)
