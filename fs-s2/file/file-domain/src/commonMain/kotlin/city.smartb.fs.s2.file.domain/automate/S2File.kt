package city.smartb.fs.s2.file.domain.automate

import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent
import kotlinx.serialization.Serializable
import s2.dsl.automate.S2Role
import s2.dsl.automate.S2State
import s2.dsl.automate.builder.s2

/**
 * Identifier of a file
 * @d2 model
 * @parent [city.smartb.fs.s2.file.domain.D2FilePage]
 * @visual json "91541047-5da8-4161-af79-3fd367fc014e"
 * @order 0
 */
typealias FileId = String

object S2 {
	val traceSourcing = s2 {
		name = "FileSourcing"
		transaction<FileInitiatedEvent> {
			to = FileState.Exists
			role = FileRole.Tracer
		}
		selfTransaction<FileLoggedEvent> {
			states += FileState.Exists
			role = FileRole.Tracer
		}
		transaction<FileDeletedEvent> {
			from = FileState.Exists
			to = FileState.Deleted
			role = FileRole.Tracer
		}
	}
}

@Serializable
open class FileRole(open val name: String): S2Role {
	object Tracer: FileRole("Tracer")
	override fun toString(): String = name
}

@Serializable
enum class FileState(override var position: Int): S2State {
	Exists(0),
	Deleted(1)
}
