package city.smartb.fs.s2.file.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
@JsName("FilePathDTO")
interface FilePathDTO {
    /**
     * String describing the object type
     * @example "MyAwesomeObject"
     */
    val objectType: String

    /**
     * Object id
     * @example "91541047-5da8-4161-af79-3fd367fc014e"
     */
    val objectId: String

    /**
     * Directory name
     * @example "image"
     */
    val directory: String

    /**
     * File name
     * @example "main.jpg"
     */
    val name: String
}

/**
 * Contains data allowing to build a path to the file
 * @d2 model
 * @title Model
 * @parent [city.smartb.fs.s2.file.domain.D2FilePathPage]
 * @visual json
 */
@Serializable
@SerialName("FilePath")
data class FilePath(
    override val objectType: String,
    override val objectId: String,
    override val directory: String,
    override val name: String
): FilePathDTO {
    companion object {
        fun from(path: String): FilePath {
            val (objectType, objectId, directory, name) = path.split("/", limit = 4)
            return FilePath(
                objectType = objectType,
                objectId = objectId,
                directory = directory,
                name = name
            )
        }
    }

    override fun toString() = "$objectType/$objectId/$directory/$name"

    fun toPartialPrefix(trailingSlash: Boolean = true) = toString()
        .substringBefore("//") // stop before first empty parameter
        .removeSuffix("/")
        .plus(if (trailingSlash) "/" else "")

    fun buildUrl(baseUrl: String, bucket: String, dnsStyle: Boolean) = if (dnsStyle) {
        val (scheme, host) = baseUrl.removeSuffix("/").split("://")
        "$scheme://$bucket.$host/$this"
    } else {
        "${baseUrl.removeSuffix("/")}/$bucket/$this"
    }
}
