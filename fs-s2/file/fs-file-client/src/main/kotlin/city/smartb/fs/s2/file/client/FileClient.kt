package city.smartb.fs.s2.file.client

import city.smartb.fs.s2.file.domain.features.command.FileDeleteCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvents
import city.smartb.fs.s2.file.domain.features.command.FileInitPublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryInitializedEvent
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryRevokedEvent
import city.smartb.fs.s2.file.domain.features.command.FileRevokePublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import city.smartb.fs.s2.file.domain.features.query.FileAskQuestionQuery
import city.smartb.fs.s2.file.domain.features.query.FileAskQuestionResult
import city.smartb.fs.s2.file.domain.features.query.FileDownloadQuery
import city.smartb.fs.s2.file.domain.features.query.FileGetQuery
import city.smartb.fs.s2.file.domain.features.query.FileGetResult
import city.smartb.fs.s2.file.domain.features.query.FileListQuery
import city.smartb.fs.s2.file.domain.features.query.FileListResult
import io.ktor.client.HttpClientConfig
import io.ktor.utils.io.ByteReadChannel

class FileClient(
    url: String,
    block: HttpClientConfig<*>.() -> Unit = {}
): Client(url, block) {
    suspend fun fileGet(command: List<FileGetQuery>): List<FileGetResult> = post("fileGet", command)

    suspend fun fileDownload(command: FileDownloadQuery): ByteReadChannel = post("fileDownload", command)

    suspend fun fileList(command: List<FileListQuery>): List<FileListResult> = post("fileList", command)

    suspend fun fileUpload(
        command: FileUploadCommand, file: ByteArray
    ): FileUploadedEvent = postFormData("fileUpload") {
        param("command", command)
        file("file", file, command.path.name)
    }

    suspend fun fileDelete(
        command: List<FileDeleteCommand>
    ): List<FileDeletedEvents> = post("fileDelete", command)

    suspend fun initPublicDirectory(
        command: List<FileInitPublicDirectoryCommand>
    ): List<FilePublicDirectoryInitializedEvent> = post("initPublicDirectory", command)

    suspend fun revokePublicDirectory(
        command: List<FileRevokePublicDirectoryCommand>
    ): List<FilePublicDirectoryRevokedEvent> = post("revokePublicDirectory", command)

    suspend fun fileAskQuestion(
        query: List<FileAskQuestionQuery>
    ): List<FileAskQuestionResult> = post("fileAskQuestion", query)

}
