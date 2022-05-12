package city.smartb.fs.s2.file.domain.model

import city.smartb.fs.s2.file.domain.automate.FileId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Descriptor of a file
 * @d2 model
 * @parent [city.smartb.fs.s2.file.domain.D2FileModelSection]
 * @order 10
 */
@Serializable
@SerialName("File")
data class File(
    /**
     * Identifier of the file
     */
    val id: FileId,

    /**
     * Path of the file within the S3 bucket
     */
    val path: FilePath,

    /**
     * External URL to access the file
     * @example "https://s3.myproject.smart-b.io/myBucket/myObjectType/ObjectId/mediaDirectory/FileName.jpg"
     */
    val url: String,

    /**
     * Additional metadata of the file
     * @example {
     *  "Pragma" : "no-cache",
     *  "Content-Type" : "image/jpeg",
     *  "secret": "blblbl"
     *  }
     */
    val metadata: Map<String, String>
)
