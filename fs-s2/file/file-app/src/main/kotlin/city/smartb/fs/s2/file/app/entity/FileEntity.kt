package city.smartb.fs.s2.file.app.entity

import city.smartb.fs.s2.file.domain.automate.FileId
import city.smartb.fs.s2.file.domain.automate.FileState
import s2.dsl.automate.model.WithS2Id
import s2.dsl.automate.model.WithS2State

class FileEntity(
    val id: FileId,
    val name: String,
    val objectId: String,
    val category: String?,
    val metadata: Map<String, String>,
    val uploadDate: Long
): WithS2State<FileState>, WithS2Id<FileId> {
	override fun s2State(): FileState = FileState.Exists
	override fun s2Id(): FileId = id

    fun path() = path(objectId, category, name)

    companion object {
        fun path(objectId: String, category: String?, name: String) = "$objectId/${category?.plus("/")}$name"
    }
}
