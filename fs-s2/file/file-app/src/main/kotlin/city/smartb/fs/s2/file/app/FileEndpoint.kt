package city.smartb.fs.s2.file.app

import city.smartb.fs.api.config.Roles
import city.smartb.fs.api.config.S3BucketProvider
import city.smartb.fs.api.config.S3Properties
import city.smartb.fs.commons.kb.KbClient
import city.smartb.fs.commons.kb.domain.command.VectorCreateCommandDTOBase
import city.smartb.fs.commons.kb.vectorCreateFunction
import city.smartb.fs.s2.file.app.config.FsSsmConfig
import city.smartb.fs.s2.file.app.model.Policy
import city.smartb.fs.s2.file.app.model.S3Action
import city.smartb.fs.s2.file.app.model.S3Effect
import city.smartb.fs.s2.file.app.model.Statement
import city.smartb.fs.s2.file.app.model.toFile
import city.smartb.fs.s2.file.app.model.toFileUploadedEvent
import city.smartb.fs.s2.file.app.service.S3Service
import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.features.command.FileDeleteByIdCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeleteFunction
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitPublicDirectoryFunction
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryInitializedEvent
import city.smartb.fs.s2.file.domain.features.command.FilePublicDirectoryRevokedEvent
import city.smartb.fs.s2.file.domain.features.command.FileRevokePublicDirectoryFunction
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent
import city.smartb.fs.s2.file.domain.features.command.FileVectorizeFunction
import city.smartb.fs.s2.file.domain.features.command.FileVectorizedEvent
import city.smartb.fs.s2.file.domain.features.query.FileDownloadQuery
import city.smartb.fs.s2.file.domain.features.query.FileGetFunction
import city.smartb.fs.s2.file.domain.features.query.FileGetResult
import city.smartb.fs.s2.file.domain.features.query.FileListFunction
import city.smartb.fs.s2.file.domain.features.query.FileListResult
import city.smartb.fs.s2.file.domain.model.File
import city.smartb.fs.s2.file.domain.model.FilePath
import city.smartb.fs.spring.utils.contentByteArray
import city.smartb.fs.spring.utils.encodeToB64
import city.smartb.fs.spring.utils.hash
import f2.dsl.fnc.f2Function
import f2.dsl.fnc.invokeWith
import f2.spring.exception.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import s2.spring.utils.logger.Logger
import java.net.URLConnection
import java.util.UUID
import javax.annotation.security.RolesAllowed

/**
 * @d2 service
 * @title File/Entrypoints
 * @parent [city.smartb.fs.s2.file.domain.D2FilePage]
 */
@RestController
@RequestMapping
@Configuration
class FileEndpoint(
    private val fileDeciderSourcingImpl: FileDeciderSourcingImpl,
    private val fsSsmConfig: FsSsmConfig,
    private val s3Properties: S3Properties,
    private val s3BucketProvider: S3BucketProvider,
    private val s3Service: S3Service,
) {
    private val logger by Logger()

    @Autowired(required = false)
    private lateinit var kbClient: KbClient

    /**
     * Fetch a given file descriptor and content
     */
    @RolesAllowed(Roles.READ_FILE)
    @Bean
    fun fileGet(): FileGetFunction = f2Function { query ->
        val path = query.toString()
        logger.info("fileGet: $path")

        val metadata = s3Service.getObjectMetadata(path)
            ?: return@f2Function FileGetResult(null, null)

        val content = s3Service.getObject(path)!!.use { input ->
            input.readBytes().encodeToB64()
        }

        FileGetResult(
            file = File(
                id = metadata["id"].orEmpty(),
                path = FilePath(
                    objectType = query.objectType,
                    objectId = query.objectId,
                    directory = query.directory,
                    name = query.name,
                ),
                url = query.buildUrl(),
                metadata = metadata
            ),
            content = content
        )
    }

    @RolesAllowed(Roles.READ_FILE)
    @PostMapping("/fileDownload", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    suspend fun fileDownload(
        @RequestBody query: FileDownloadQuery,
        response: ServerHttpResponse
    ): ByteArray? {
        val path = FilePath(
            objectType = query.objectType,
            objectId = query.objectId,
            directory = query.directory,
            name = query.name,
        ).toString()
        logger.info("fileDownload: $path")

        response.headers.contentType = URLConnection.guessContentTypeFromName(query.name)
            ?.split("/")
            ?.takeIf { it.size == 2 }
            ?.let { (type, subtype) -> MediaType(type, subtype) }
            ?: MediaType.APPLICATION_OCTET_STREAM

        return withContext(Dispatchers.IO) {
            s3Service.getObject(path)?.readAllBytes()
        }
    }

    /**
     * Fetch a list of file descriptors
     */
    @RolesAllowed(Roles.READ_FILE)
    @Bean
    fun fileList(): FileListFunction = f2Function { query ->
        logger.info("fileList: $query")
        val prefix = FilePath(
            objectType = query.objectType,
            objectId = query.objectId,
            directory = query.directory ?: "",
            name = ""
        ).toPartialPrefix()

        s3Service.listObjects(prefix)
            .map { obj -> obj.get().toFile { it.buildUrl() } }
            .let(::FileListResult)
    }

    /**
     * Upload multiple files
     * @d2 command
     */
    @RolesAllowed(Roles.WRITE_FILE)
    @PostMapping("/fileUploads")
    fun fileUploads(
        @RequestPart("command") commands: HashMap<String, FileUploadCommand>,
        @RequestPart("file") files: Flux<FilePart>
    ) : Flux<FileUploadedEvent> {
        return files.map { file ->
            val cmd = commands[file.filename()]!! // TODO throw readable error if no command found for file
            fileUpload(cmd, file)
        }
    }

    /**
     * Upload a file
     */
    @RolesAllowed(Roles.WRITE_FILE)
    @PostMapping("/fileUpload")
    fun fileUpload(
        @RequestPart("command") cmd: FileUploadCommand,
        @RequestPart("file") file: FilePart
    ): FileUploadedEvent = runBlocking {
        val pathStr = cmd.path.toString()
        logger.info("fileUpload: $cmd")
        val fileMetadata = s3Service.getObjectMetadata(pathStr)

        val fileExists = fileMetadata != null
        val fileId = fileMetadata?.get("id") ?: UUID.randomUUID().toString()

        val fileByteArray = file.contentByteArray()

        s3Service.putObject(
            path = pathStr,
            content = fileByteArray,
            metadata = cmd.metadata.plus("id" to fileId)
        )

        if (cmd.vectorize) {
            vectorize(cmd.path, cmd.metadata, fileByteArray)
        }

        if (mustBeSavedToSsm(cmd.path.directory)) {
            if (fileExists) {
                fileByteArray.logFileInSsm(cmd, fileId, cmd.path.buildUrl())
            } else {
                fileByteArray.initFileInSsm(cmd, fileId, cmd.path.buildUrl())
            }
        } else {
            FileUploadedEvent(
                id = fileId,
                path = cmd.path,
                url = cmd.path.buildUrl(),
                hash = fileByteArray.hash(),
                metadata = cmd.metadata,
                time = System.currentTimeMillis()
            )
        }
    }

    private suspend fun vectorize(
        path: FilePath,
        metadata: Map<String, String>,
        fileByteArray: ByteArray
    ) {
        logger.debug("Vectorizing file $path")
        VectorCreateCommandDTOBase(
            path = path,
            file = fileByteArray,
            metadata = metadata
        ).invokeWith(kbClient.vectorCreateFunction())
        logger.debug("File $path vectorized")
    }

    /**
     * Delete a file
     */
    @RolesAllowed(Roles.WRITE_FILE)
    @Bean
    fun fileDelete(): FileDeleteFunction = f2Function { cmd ->
        val pathStr = cmd.toString()
        logger.info("fileDelete: $pathStr")

        val metadata = s3Service.getObjectMetadata(pathStr)
            ?: throw IllegalArgumentException("File not found at path [$pathStr]")

        val id = metadata["id"]!!

        s3Service.removeObject(pathStr)
        if (mustBeSavedToSsm(cmd.directory)) {
            fileDeciderSourcingImpl.delete(FileDeleteByIdCommand(id = id))
        }

        FileDeletedEvent(
            id = id,
            path = cmd
        )
    }

    /**
     * Vectorize a file and save it into a vector-store
     */
    @RolesAllowed(Roles.WRITE_FILE)
    @Bean
    fun fileVectorize(): FileVectorizeFunction = f2Function { cmd ->
        logger.info("fileVectorize: ${cmd.path}")

        val fileContent = withContext(Dispatchers.IO) {
            s3Service.getObject(cmd.path.toString())?.readAllBytes()
        } ?: throw NotFoundException("File", cmd.path.toString())

        vectorize(cmd.path, cmd.metadata, fileContent)

        FileVectorizedEvent(cmd.path)
    }

    /**
     * Grant public access to a given directory
     */
    @RolesAllowed(Roles.WRITE_POLICY)
    @Bean
    fun initPublicDirectory(): FileInitPublicDirectoryFunction = f2Function { cmd ->
        val path = FilePath(
            objectType = cmd.objectType,
            objectId = cmd.objectId,
            directory = cmd.directory,
            name = "*"
        ).toString()
        logger.info("initPublicDirectory: $path")

        val policy = s3Service.getBucketPolicy().orEmpty()
        policy.getOrAddStatementWith(S3Effect.ALLOW, S3Action.GET_OBJECT)
            .addResource(bucket = s3BucketProvider.getBucket(), path = path)
        s3Service.setBucketPolicy(policy)

        FilePublicDirectoryInitializedEvent(
            path = Statement.resourcePath(bucket = s3BucketProvider.getBucket(), path = path)
        )
    }

    /**
     * Revoke public access to a given directory
     */
    @RolesAllowed(Roles.WRITE_POLICY)
    @Bean
    fun revokePublicDirectory(): FileRevokePublicDirectoryFunction = f2Function { cmd ->
        val path = FilePath(
            objectType = cmd.objectType,
            objectId = cmd.objectId,
            directory = cmd.directory,
            name = "*"
        ).toString()
        logger.info("revokePublicDirectory: $path")

        val policy = s3Service.getBucketPolicy().orEmpty()
        policy.getStatementWith(S3Effect.ALLOW, S3Action.GET_OBJECT)
            ?.removeResource(bucket = s3BucketProvider.getBucket(), path = path)
        s3Service.setBucketPolicy(policy)

        FilePublicDirectoryRevokedEvent(
            path = Statement.resourcePath(bucket = s3BucketProvider.getBucket(), path = path)
        )
    }

    private fun mustBeSavedToSsm(category: String?) = category in fsSsmConfig.directories.orEmpty()

    private suspend fun ByteArray.initFileInSsm(cmd: FileUploadCommand, fileId: FileId, path: String): FileUploadedEvent {
        return FileInitCommand(
            id = fileId,
            path = cmd.path,
            url = path,
            hash = hash(),
            metadata = cmd.metadata,
        ).let { fileDeciderSourcingImpl.init(it).toFileUploadedEvent() }
    }

    private suspend fun ByteArray.logFileInSsm(cmd: FileUploadCommand, fileId: FileId, path: String): FileUploadedEvent {
        return FileLogCommand(
            id = fileId,
            path = path,
            hash = hash(),
            metadata = cmd.metadata,
        ).let { fileDeciderSourcingImpl.log(it).toFileUploadedEvent() }
    }


    private suspend fun FilePath.buildUrl() = buildUrl(s3Properties.externalUrl, s3BucketProvider.getBucket(), s3Properties.dns)


    private fun Policy?.orEmpty() = this ?: Policy()
}
