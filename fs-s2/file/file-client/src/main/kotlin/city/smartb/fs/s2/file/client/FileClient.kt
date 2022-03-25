package city.smartb.fs.s2.file.client

import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent

class FileClient(
    url: String
): Client(url) {
    suspend fun uploadFile(command: List<FileUploadCommand>): List<FileUploadedEvent> = post("uploadFile", command)
}
