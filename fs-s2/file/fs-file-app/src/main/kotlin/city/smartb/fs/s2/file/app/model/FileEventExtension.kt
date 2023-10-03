package city.smartb.fs.s2.file.app.model

import city.smartb.fs.s2.file.domain.features.command.FileInitiatedEvent
import city.smartb.fs.s2.file.domain.features.command.FileLoggedEvent
import city.smartb.fs.s2.file.domain.features.command.FileUploadedEvent

fun FileInitiatedEvent.toFileUploadedEvent() = FileUploadedEvent(
    id = id,
    path = path,
    url = url,
    hash = hash,
    metadata = metadata,
    time = time
)

fun FileLoggedEvent.toFileUploadedEvent() = FileUploadedEvent(
    id = id,
    path = path,
    url = url,
    hash = hash,
    metadata = metadata,
    time = time
)
