package city.smartb.fs.s2.file.app.view

import city.smartb.fs.s2.file.app.entity.FileEntity
import city.smartb.fs.s2.file.domain.features.command.FileEvent
import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent
import s2.sourcing.dsl.view.View

class FileModelView: View<FileEvent, FileEntity> {

	override suspend fun evolve(event: FileEvent, model: FileEntity?): FileEntity? = when (event) {
		is FileInitiatedEvent -> created(event)
		is FileLoggedEvent -> model.logged(event)
	}

	private fun created(event: FileInitiatedEvent): FileEntity {
		return FileEntity(
			id = event.id,
			name = event.name,
			objectId = event.objectId,
			category = event.category,
			metadata = event.metadata,
			uploadDate = event.time
		)
	}

	private fun FileEntity?.logged(event: FileLoggedEvent): FileEntity? = this?.let {
		FileEntity(
			id = event.id,
			name = event.name,
			objectId = event.objectId,
			category = event.category,
			metadata = event.metadata,
			uploadDate = event.time
		)
	}

}
