package city.smartb.fs.s2.file.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
@JsName("FilePathDTO")
interface FilePathDTO {
    val objectType: String
    val objectId: String
    val directory: String
    val name: String
}

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
