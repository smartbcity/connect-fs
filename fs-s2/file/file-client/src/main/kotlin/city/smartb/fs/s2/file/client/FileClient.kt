package city.smartb.fs.s2.file.client

import city.smartb.fs.s2.file.domain.features.command.FileDeleteCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitPublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryInitializedEvent
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryRevokedEvent
import city.smartb.fs.s2.file.domain.features.command.FileRevokePublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import city.smartb.fs.s2.file.domain.features.query.FileGetQuery
import city.smartb.fs.s2.file.domain.features.query.FileGetResult
import city.smartb.fs.s2.file.domain.features.query.FileListCommand
import city.smartb.fs.s2.file.domain.features.query.FileListResult

class FileClient(
    url: String
): Client(url) {
    suspend fun fileGet(command: List<FileGetQuery>): List<FileGetResult> = post("fileGet", command)

    suspend fun fileList(command: List<FileListCommand>): List<FileListResult> = post("fileList", command)

    suspend fun fileUpload(command: List<FileUploadCommand>): List<FileUploadedEvent> = post("fileUpload", command)

    suspend fun fileDelete(command: List<FileDeleteCommand>): List<FileDeletedEvent> = post("fileDelete", command)

    suspend fun initPublicDirectory(
        command: List<FileInitPublicDirectoryCommand>
    ): List<FilePublicDirectoryInitializedEvent> = post("initPublicDirectory", command)

    suspend fun revokePublicDirectory(
        command: List<FileRevokePublicDirectoryCommand>
    ): List<FilePublicDirectoryRevokedEvent> = post("revokePublicDirectory", command)
}
