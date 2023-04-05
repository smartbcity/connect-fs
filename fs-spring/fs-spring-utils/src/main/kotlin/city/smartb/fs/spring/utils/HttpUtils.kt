package city.smartb.fs.spring.utils

import city.smartb.fs.s2.file.client.FileClient
import city.smartb.fs.s2.file.domain.features.query.FileDownloadQuery
import city.smartb.fs.s2.file.domain.model.FilePathDTO
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import java.net.URLConnection

suspend fun ServerHttpResponse.serveFile(
    fileClient: FileClient,
    getFilePath: suspend () -> FilePathDTO?
): ByteArray? {
    val path = getFilePath()
        ?: return null

    configureHeadersForFile(path.name)

    return FileDownloadQuery(
        objectType = path.objectType,
        objectId = path.objectId,
        directory = path.directory,
        name = path.name
    ).let { fileClient.fileDownload(it) }
}

fun ServerHttpResponse.configureHeadersForFile(name: String) {
    headers.contentDisposition = ContentDisposition.attachment().filename(name).build()
    headers.contentType = URLConnection.guessContentTypeFromName(name)
        ?.split("/")
        ?.takeIf { it.size == 2 }
        ?.let { (type, subtype) -> MediaType(type, subtype) }
        ?: MediaType.APPLICATION_OCTET_STREAM
}
