package farid.guliyev.mblockly.utils

import java.io.File

object ProjectPathUtils {
    
    /**
     * Get the media directory path for a specific project
     * @param projectName The name of the project
     * @return The path to the project's media directory
     */
    fun getProjectMediaPath(projectName: String): String {
        return "media/$projectName"
    }
    
    /**
     * Get the images directory path for a specific project
     * @param projectName The name of the project
     * @return The path to the project's images directory
     */
    fun getProjectImagesPath(projectName: String): String {
        return "${getProjectMediaPath(projectName)}/images"
    }
    
    /**
     * Get the audios directory path for a specific project
     * @param projectName The name of the project
     * @return The path to the project's audios directory
     */
    fun getProjectAudiosPath(projectName: String): String {
        return "${getProjectMediaPath(projectName)}/audios"
    }
    
    /**
     * Get the File object for the project's images directory
     * @param filesDir The app's files directory
     * @param projectName The name of the project
     * @return File object pointing to the project's images directory
     */
    fun getProjectImagesDir(filesDir: File, projectName: String): File {
        return File(filesDir, getProjectImagesPath(projectName))
    }
    
    /**
     * Get the File object for the project's audios directory
     * @param filesDir The app's files directory
     * @param projectName The name of the project
     * @return File object pointing to the project's audios directory
     */
    fun getProjectAudiosDir(filesDir: File, projectName: String): File {
        return File(filesDir, getProjectAudiosPath(projectName))
    }
    
    /**
     * Get the File object for a specific image file in the project
     * @param filesDir The app's files directory
     * @param projectName The name of the project
     * @param imageName The name of the image file
     * @return File object pointing to the specific image file
     */
    fun getProjectImageFile(filesDir: File, projectName: String, imageName: String): File {
        return File(filesDir, "${getProjectImagesPath(projectName)}/$imageName")
    }
    
    /**
     * Get the File object for a specific audio file in the project
     * @param filesDir The app's files directory
     * @param projectName The name of the project
     * @param audioName The name of the audio file
     * @return File object pointing to the specific audio file
     */
    fun getProjectAudioFile(filesDir: File, projectName: String, audioName: String): File {
        return File(filesDir, "${getProjectAudiosPath(projectName)}/$audioName")
    }
}
