package farid.guliyev.mblockly.ui.screens.assets_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.content_resolver.getNameFromUri
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.core.file.withoutExtension
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.ui.navigation.AssetsRoute
import farid.guliyev.mblockly.utils.ProjectPathUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID

data class RenameSheetState(
    val assetId: String,
    val currentName: String
)

class AssetsViewModel constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    
    val state = MutableStateFlow(AssetsState())
    val projectName = savedStateHandle.toRoute<AssetsRoute>().projectName
    private val navigationController: NavigationController = NavigationModule.navController
    
    fun loadProjectAssets(context: Context) {
        runSafelyInBg {
            val images = mutableListOf<AssetItem>()
            val audioFiles = mutableListOf<AssetItem>()
            
            // Load images
            val imagesDir = ProjectPathUtils.getProjectImagesDir(context.filesDir, projectName)
            if (imagesDir.exists()) {
                imagesDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        images.add(
                            AssetItem(
                                id = "img_${file.name.hashCode()}",
                                name = file.name,
                                size = formatFileSize(file.length()),
                                type = AssetType.IMAGE,
                                filePath = file.absolutePath
                            )
                        )
                    }
                }
            }
            
            // Load audio files
            val audioDir = ProjectPathUtils.getProjectAudiosDir(context.filesDir, projectName)
            if (audioDir.exists()) {
                audioDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        audioFiles.add(
                            AssetItem(
                                id = "audio_${file.name.hashCode()}",
                                name = file.name,
                                size = formatFileSize(file.length()),
                                type = AssetType.AUDIO,
                                filePath = file.absolutePath
                            )
                        )
                    }
                }
            }
            
            state.update { 
                it.copy(
                    images = images,
                    audioFiles = audioFiles
                )
            }
        }
    }
    
    fun getImagePickerIntent(): Intent {
        val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        return Intent.createChooser(imagePickerIntent, "Choose an image")
    }
    
    fun uploadImage(context: Context, uri: Uri?) {
        runSafelyInBg {
            if (uri == null) failGracefully("No image selected")
            
            val originalFileName = context.contentResolver.getNameFromUri(uri = uri).orEmpty()
            if (originalFileName.isEmpty()) failGracefully("Could not get file name")
            
            // Ask user for custom name
            val customName = showInputConfirmation(
                "What would you like to name this image?", 
                initialValue = originalFileName.withoutExtension()
            )
            
            val fileExtension = originalFileName.substringAfterLast('.', "")
            val finalFileName = if (fileExtension.isNotEmpty()) "$customName.$fileExtension" else customName
            
            // Create project directory if it doesn't exist
            val projectDir = ProjectPathUtils.getProjectImagesDir(context.filesDir, projectName)
            if (!projectDir.exists()) {
                projectDir.mkdirs()
            }
            
            // Copy file to project directory
            val destinationFile = File(projectDir, finalFileName)
            context.contentResolver.openInputStream(uri)!!.buffered().use { input ->
                destinationFile.outputStream().buffered().use { output ->
                    output.write(input.readBytes())
                }
            }
            
            // Get file size for display
            val fileSize = formatFileSize(destinationFile.length())
            
            val newImage = AssetItem(
                id = "img_${UUID.randomUUID().toString().take(8)}",
                name = finalFileName,
                size = fileSize,
                type = AssetType.IMAGE,
                filePath = destinationFile.absolutePath
            )
            
            state.update { 
                it.copy(images = it.images + newImage)
            }
            
            showSuccessAlert("Image uploaded successfully!")
        }
    }
    
    fun getAudioPickerIntent(): Intent {
        val audioPickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*"
        }
        return Intent.createChooser(audioPickerIntent, "Choose an audio file")
    }
    
    fun uploadAudio(context: Context, uri: Uri?) {
        runSafelyInBg {
            if (uri == null) failGracefully("No audio file selected")
            
            val originalFileName = context.contentResolver.getNameFromUri(uri = uri).orEmpty()
            if (originalFileName.isEmpty()) failGracefully("Could not get file name")
            
            // Ask user for custom name
            val customName = showInputConfirmation(
                "What would you like to name this audio file?", 
                initialValue = originalFileName.withoutExtension()
            )
            
            val fileExtension = originalFileName.substringAfterLast('.', "")
            val finalFileName = if (fileExtension.isNotEmpty()) "$customName.$fileExtension" else customName
            
            // Create project directory if it doesn't exist
            val projectDir = ProjectPathUtils.getProjectAudiosDir(context.filesDir, projectName)
            if (!projectDir.exists()) {
                projectDir.mkdirs()
            }
            
            // Copy file to project directory
            val destinationFile = File(projectDir, finalFileName)
            context.contentResolver.openInputStream(uri)!!.buffered().use { input ->
                destinationFile.outputStream().buffered().use { output ->
                    output.write(input.readBytes())
                }
            }
            
            // Get file size for display
            val fileSize = formatFileSize(destinationFile.length())
            
            val newAudio = AssetItem(
                id = "audio_${UUID.randomUUID().toString().take(8)}",
                name = finalFileName,
                size = fileSize,
                type = AssetType.AUDIO,
                filePath = destinationFile.absolutePath
            )
            
            state.update { 
                it.copy(audioFiles = it.audioFiles + newAudio)
            }
            
            showSuccessAlert("Audio file uploaded successfully!")
        }
    }
    
    fun deleteImage(context: Context, imageId: String) {
        runSafelyInBg {
            val imageToDelete = state.value.images.find { it.id == imageId }
            if (imageToDelete != null) {
                // Delete file from filesystem
                val imageFile = ProjectPathUtils.getProjectImageFile(context.filesDir, projectName, imageToDelete.name)
                if (imageFile.exists()) {
                    imageFile.delete()
                }
                
                // Remove from state
                state.update { 
                    it.copy(images = it.images.filter { image -> image.id != imageId })
                }
                showSuccessAlert("Image deleted successfully!")
            }
        }
    }
    
    fun deleteAudio(context: Context, audioId: String) {
        runSafelyInBg {
            val audioToDelete = state.value.audioFiles.find { it.id == audioId }
            if (audioToDelete != null) {
                // Delete file from filesystem
                val audioFile = ProjectPathUtils.getProjectAudioFile(context.filesDir, projectName, audioToDelete.name)
                if (audioFile.exists()) {
                    audioFile.delete()
                }
                
                // Remove from state
                state.update { 
                    it.copy(audioFiles = it.audioFiles.filter { audio -> audio.id != audioId })
                }
                showSuccessAlert("Audio file deleted successfully!")
            }
        }
    }

    // TODO - needs optimization ASAP!
    fun renameAsset(currentName: String, assetId: String) {

        runSafelyInBg {
            val newName = showInputConfirmation(description = "Enter new name", initialValue = currentName.withoutExtension())
            val fileExtension = currentName.substringAfterLast('.', "")
            val finalName = if (fileExtension.isNotEmpty()) "$newName.$fileExtension" else newName
            
            // Update the asset name in state
            state.update { currentState ->
                val updatedImages = currentState.images.map { image ->
                    if (image.id == assetId) {
                        image.copy(name = finalName)
                    } else {
                        image
                    }
                }
                
                val updatedAudioFiles = currentState.audioFiles.map { audio ->
                    if (audio.id == assetId) {
                        audio.copy(name = finalName)
                    } else {
                        audio
                    }
                }
                
                currentState.copy(
                    images = updatedImages,
                    audioFiles = updatedAudioFiles
                )
            }
            
            // Rename the actual file
            val asset = state.value.images.find { it.id == assetId }
                ?: state.value.audioFiles.find { it.id == assetId }
            
            if (asset != null && asset.filePath != null) {
                val oldFile = File(asset.filePath)
                val newFile = File(oldFile.parent, finalName)
                
                if (oldFile.exists() && oldFile.renameTo(newFile)) {
                    showSuccessAlert("File renamed successfully!")
                } else {
                    showErrorAlert(Exception("Failed to rename file"))
                }
            }
        }
    }
    
    fun goBack() {
        navigationController.sendCommand { popBackStack() }
    }
    
    private fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }
}

