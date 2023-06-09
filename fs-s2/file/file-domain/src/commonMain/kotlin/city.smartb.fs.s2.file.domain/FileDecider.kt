package city.smartb.fs.s2.file.domain

import city.smartb.fs.s2.file.domain.features.command.FileDeleteByIdCommand
import city.smartb.fs.s2.file.domain.features.command.FileDeletedEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent

interface FileDecider {
	suspend fun init(cmd: FileInitCommand): FileInitiatedEvent
	suspend fun log(cmd: FileLogCommand): FileLoggedEvent
	suspend fun delete(cmd: FileDeleteByIdCommand): FileDeletedEvent
}
