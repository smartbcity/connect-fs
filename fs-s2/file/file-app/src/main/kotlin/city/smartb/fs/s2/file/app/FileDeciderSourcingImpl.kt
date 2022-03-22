package city.smartb.fs.s2.file.app

import city.smartb.fs.s2.file.app.config.FileSourcingS2Decider
import city.smartb.fs.s2.file.app.config.S3Config
import city.smartb.fs.s2.file.domain.FileDecider
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.Base64

@Service
class FileDeciderSourcingImpl(
	private val decider: FileSourcingS2Decider,
): FileDecider {

	override suspend fun init(cmd: FileInitCommand): FileInitiatedEvent = decider.init(cmd) {
		FileInitiatedEvent(
			id = cmd.id,
			name = cmd.name,
			objectId = cmd.objectId,
			category = cmd.category,
			path = cmd.path,
			hash = cmd.hash,
			metadata = cmd.metadata,
			time = System.currentTimeMillis(),
		)
	}

	override suspend fun log(cmd: FileLogCommand): FileLoggedEvent = decider.transition(cmd) { file ->
		FileLoggedEvent(
			id = cmd.id,
			name = file.name,
			objectId = file.objectId,
			category = file.category,
			path = cmd.path,
			hash = cmd.hash,
			metadata = cmd.metadata,
			time = System.currentTimeMillis(),
		)
	}
}
