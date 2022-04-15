package city.smartb.fs.s2.file.client

import city.smartb.fs.s2.file.domain.features.command.FileDeleteCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitPublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryInitializedEvent
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryRevokedEvent
import city.smartb.fs.s2.file.domain.features.command.FileRevokePublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import city.smartb.fs.s2.file.domain.features.query.FileGetListCommand
import city.smartb.fs.s2.file.domain.features.query.FileGetListResult
import city.smartb.fs.s2.file.domain.features.query.FileGetQuery
import city.smartb.fs.s2.file.domain.features.query.FileGetResult

class FileClient(
    url: String
): Client(url) {
    suspend fun getFile(command: List<FileGetQuery>): List<FileGetResult> = post("getFile", command)
    suspend fun listFiles(command: List<FileGetListCommand>): List<FileGetListResult> = post("listFiles", command)
    suspend fun uploadFile(command: List<FileUploadCommand>): List<FileUploadedEvent> = post("uploadFile", command)
    suspend fun deleteFile(command: List<FileDeleteCommand>): List<FileDeletedEvent> = post("deleteFile", command)
    suspend fun initPublicDirectory(
        command: List<FileInitPublicDirectoryCommand>
    ): List<FilePublicDirectoryInitializedEvent> = post("initPublicDirectory", command)

    suspend fun revokePublicDirectory(
        command: List<FileRevokePublicDirectoryCommand>
    ): List<FilePublicDirectoryRevokedEvent> = post("revokePublicDirectory", command)
}
