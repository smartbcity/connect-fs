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

/**
 * @d2 model
 * @parent [city.smartb.fs.s2.file.client.D2FileClientSection]
 */
class FileClient(
    url: String
): Client(url) {
    /**
     * Fetch specific file from a bucket
     */
    suspend fun getFile(command: List<FileGetQuery>): List<FileGetResult> = post("getFile", command)

    /**
     * Fetch the list of files from a bucket
     */
    suspend fun listFiles(command: List<FileGetListCommand>): List<FileGetListResult> = post("listFiles", command)

    /**
     * Upload the file to a S3 bucket
     */
    suspend fun uploadFile(command: List<FileUploadCommand>): List<FileUploadedEvent> = post("uploadFile", command)

    /**
     * Delete the file from S3 bucket
     */
    suspend fun deleteFile(command: List<FileDeleteCommand>): List<FileDeletedEvent> = post("deleteFile", command)

    /**
     * Create the public directory
     */
    suspend fun initPublicDirectory(
        command: List<FileInitPublicDirectoryCommand>
    ): List<FilePublicDirectoryInitializedEvent> = post("initPublicDirectory", command)

    /**
     * Revoke the public directory
     */
    suspend fun revokePublicDirectory(
        command: List<FileRevokePublicDirectoryCommand>
    ): List<FilePublicDirectoryRevokedEvent> = post("revokePublicDirectory", command)
}
