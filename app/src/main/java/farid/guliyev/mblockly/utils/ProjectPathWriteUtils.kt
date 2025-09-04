package farid.guliyev.mblockly.utils

import android.content.Context
import farid.guliyev.mblockly.core.MB_EXTENSION_FULL
import java.io.File

fun Context.createNewProject(projectName: String) : File {
    val projectDir = getProjectDir(projectName).also {
        val isCreated = it.mkdir()
        if (!isCreated) error("Project named $projectName already exists")
    }
    val file = File(projectDir, projectName + MB_EXTENSION_FULL).also { it.createNewFile() }
    return file
}

fun Context.deleteProject(projectName: String) {
    val projectDir = getProjectDir(projectName)
    assert(projectDir.deleteRecursively())
}