package city.smartb.fs.s2.file.domain.model

import city.smartb.fs.s2.file.domain.automate.FileId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Descriptor of a file
 * @d2 model
 * @parent [city.smartb.fs.s2.file.domain.D2FilePage]
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
    val metadata: Map<String, String>,

    /**
     * Size of the file in bytes
     * @example 69950
     */
    val size: Long,

    /**
     * Whether the file has been vectorized and sent to a knowledge-base
     * @example false
     */
    val vectorized: Boolean,

    /**
     * Date of the last modification of the file, formatted as UNIX timestamp in ms
     * @example 1692627519000
     */
    val lastModificationDate: Long
)
