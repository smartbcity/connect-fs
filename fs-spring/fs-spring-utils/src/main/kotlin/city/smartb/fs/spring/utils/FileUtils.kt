package city.smartb.fs.spring.utils

import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.model.FilePath
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.Base64


fun FilePath.toUploadCommand() = FileUploadCommand(
    path = this,
    metadata = mapOf(
        "uploadedAt" to System.currentTimeMillis().toString()
    )
)

fun ByteArray.hash() = MessageDigest
    .getInstance("SHA-256")
    .digest(this)
    .encodeToB64()

fun ByteArray.encodeToB64() = Base64.getEncoder().encodeToString(this)
fun String.decodeB64() = Base64.getDecoder().decode(substringAfterLast(";base64,"))

suspend fun FilePart.contentByteArray(): ByteArray {
    return ByteArrayOutputStream().use { os ->
        DataBufferUtils.write(content(), os).awaitLast()
        os.toByteArray()
    }
}
