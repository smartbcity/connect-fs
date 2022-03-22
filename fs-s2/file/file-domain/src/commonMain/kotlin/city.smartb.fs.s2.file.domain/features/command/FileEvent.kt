package city.smartb.fs.s2.file.domain.features.command

import city.smartb.fs.s2.file.domain.automate.FileId
import f2.dsl.cqrs.Event
import s2.dsl.automate.model.WithS2Id

sealed interface FileEvent: Event, WithS2Id<FileId>
