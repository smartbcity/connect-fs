package city.smartb.fs.s2.file.app.config

import city.smartb.fs.s2.file.app.entity.FileEntity
import city.smartb.fs.s2.file.app.view.FileModelView
import city.smartb.fs.s2.file.app.view.RedisSnapView
import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.automate.FileState
import city.smartb.fs.s2.file.domain.automate.S2
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import s2.spring.automate.sourcing.S2AutomateDeciderSpring
import s2.spring.sourcing.ssm.S2SourcingSsmAdapter
import ssm.chaincode.dsl.model.Agent
import ssm.chaincode.dsl.model.uri.ChaincodeUri
import ssm.chaincode.dsl.model.uri.from
import ssm.sdk.sign.extention.loadFromFile
import kotlin.reflect.KClass

@Configuration
class FileSourcingAutomateConfig(
	private val fsSsmConfig: FsSsmConfig,
	redisSnapView: RedisSnapView,
	traceS2Decider: FileSourcingS2Decider
): S2SourcingSsmAdapter<FileEntity, FileState, FileEvent, FileId, FileSourcingS2Decider>(
	executor = traceS2Decider,
	view = FileModelView(),
	snapRepository = redisSnapView
) {

	override fun entityType(): KClass<FileEvent> = FileEvent::class

	override fun json(): Json = Json {
		serializersModule = SerializersModule {
			polymorphic(FileEvent::class) {
				subclass(FileInitiatedEvent::class, FileInitiatedEvent.serializer())
				subclass(FileLoggedEvent::class, FileLoggedEvent.serializer())
				subclass(FileDeletedEvent::class, FileDeletedEvent.serializer())
			}
		}
	}

	override fun chaincodeUri() = ChaincodeUri.from(channelId = fsSsmConfig.channel, chaincodeId = fsSsmConfig.chaincode)
	override fun signerAgent() = Agent.loadFromFile(fsSsmConfig.signerName, fsSsmConfig.signerFile)
	override fun automate() = S2.traceSourcing
}

@Service
class FileSourcingS2Decider: S2AutomateDeciderSpring<FileEntity, FileState, FileEvent, FileId>()
