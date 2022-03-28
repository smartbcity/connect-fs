package city.smartb.fs.s2.file.app.model

object FilePathUtils {

    fun buildRelativePath(objectId: String, category: String?, name: String) = "$objectId/${category?.plus("/")}$name"

    fun parseRelativePath(path: String): Triple<String, String?, String> {
        val (objectId, subPath) = path.split("/", limit = 2)
        val category = subPath.substringBefore("/", "").ifBlank { null }
        val name = subPath.substringAfter("/")

        return Triple(objectId, category, name)
    }

    fun buildAbsolutePath(relativePath: String, baseUrl: String, bucket: String, dnsStyle: Boolean) = if (dnsStyle) {
        val (scheme, host) = baseUrl.removeSuffix("/").split("://")
        "$scheme://$bucket.$host/$relativePath"
    } else {
        "$baseUrl/$bucket/$relativePath"
    }

    fun parseAbsolutePath(path: String, baseUrl: String, bucket: String, dnsStyle: Boolean) = if (dnsStyle) {
        val (_, host) = baseUrl.removeSuffix("/").split("://")
        val relativePath = path.substringAfter(host).removePrefix("/")
        parseRelativePath(relativePath)
    } else {
        val relativePath = path.substringAfter(baseUrl).removePrefix("/").substringAfter("$bucket/")
        parseRelativePath(relativePath)
    }
}
