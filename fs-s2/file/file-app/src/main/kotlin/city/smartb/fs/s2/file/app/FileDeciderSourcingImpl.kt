package city.smartb.fs.s2.file.app

import city.smartb.fs.s2.file.app.config.FileSourcingS2Decider
import city.smartb.fs.s2.file.app.config.S3Config
import city.smartb.fs.s2.file.domain.FileDecider
import city.smartb.fs.s2.file.domain.features.command.FileDeleteByIdCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent
import city.smartb.fs.s2.file.domain.model.FilePath
import org.springframework.stereotype.Service

@Service
class FileDeciderSourcingImpl(
	private val decider: FileSourcingS2Decider,
	private val s3Config: S3Config
): FileDecider {

	override suspend fun init(cmd: FileInitCommand): FileInitiatedEvent = decider.init(cmd) {
		FileInitiatedEvent(
			id = cmd.id,
			path = cmd.path,
			url = cmd.url,
			hash = cmd.hash,
			metadata = cmd.metadata,
			time = System.currentTimeMillis(),
		)
	}

	override suspend fun log(cmd: FileLogCommand): FileLoggedEvent = decider.transition(cmd) { file ->
		val path = FilePath.from(cmd.path)
		FileLoggedEvent(
			id = file.id,
			path = path,
			url = path.buildUrl(),
			hash = cmd.hash,
			metadata = cmd.metadata,
			time = System.currentTimeMillis(),
		)
	}

	override suspend fun delete(cmd: FileDeleteByIdCommand): FileDeletedEvent = decider.transition(cmd) { file ->
		FileDeletedEvent(
			id = file.id,
			path = FilePath(
				objectType = file.objectType,
				objectId = file.objectId,
				directory = file.directory,
				name = file.name
			)
		)
	}

	private fun FilePath.buildUrl() = buildUrl(s3Config.externalUrl, s3Config.bucket, s3Config.dns)
}
