package farid.guliyev.mblockly.utils

import android.content.Context
import farid.guliyev.mblockly.core.MB_EXTENSION_FULL
import java.io.File

fun Context.getProjectDir(projectName: String) : File {
    return File(filesDir, projectName)
}
fun Context.getProjectMBFile(projectName: String) : File {
    return File(getProjectDir(projectName), projectName + MB_EXTENSION_FULL)
}
fun getProjectMediaPath(projectName: String): String {
    return "$projectName/media"
}
fun getProjectImagesPath(projectName: String): String {
    return "${getProjectMediaPath(projectName)}/images"
}
fun getProjectAudiosPath(projectName: String): String {
    return "${getProjectMediaPath(projectName)}/audios"
}
fun Context.getProjectImagesDir(projectName: String): File {
    return File(filesDir, getProjectImagesPath(projectName))
}

fun Context.getProjectAudiosDir(projectName: String): File {
    return File(filesDir, getProjectAudiosPath(projectName))
}
fun Context.getProjectImageFile(projectName: String, imageName: String): File {
    return File(filesDir, "${getProjectImagesPath(projectName)}/$imageName")
}
fun Context.getProjectAudioFile(projectName: String, audioName: String): File {
    return File(filesDir, "${getProjectAudiosPath(projectName)}/$audioName")
}
