package farid.guliyev.mblockly.ui.screens.assets_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.content_resolver.getNameFromUri
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.ui.navigation.AssetsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID

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
            val imagesDir = File(context.filesDir, "images/$projectName")
            if (imagesDir.exists()) {
                imagesDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        images.add(
                            AssetItem(
                                id = "img_${file.name.hashCode()}",
                                name = file.name,
                                size = formatFileSize(file.length()),
                                type = AssetType.IMAGE
                            )
                        )
                    }
                }
            }
            
            // Load audio files
            val audioDir = File(context.filesDir, "audios/$projectName")
            if (audioDir.exists()) {
                audioDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        audioFiles.add(
                            AssetItem(
                                id = "audio_${file.name.hashCode()}",
                                name = file.name,
                                size = formatFileSize(file.length()),
                                type = AssetType.AUDIO
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
            
            val fileName = context.contentResolver.getNameFromUri(uri = uri).orEmpty()
            if (fileName.isEmpty()) failGracefully("Could not get file name")
            
            // Create project directory if it doesn't exist
            val projectDir = File(context.filesDir, "images/$projectName")
            if (!projectDir.exists()) {
                projectDir.mkdirs()
            }
            
            // Copy file to project directory
            val destinationFile = File(projectDir, fileName)
            context.contentResolver.openInputStream(uri)!!.buffered().use { input ->
                destinationFile.outputStream().buffered().use { output ->
                    output.write(input.readBytes())
                }
            }
            
            // Get file size for display
            val fileSize = formatFileSize(destinationFile.length())
            
            val newImage = AssetItem(
                id = "img_${UUID.randomUUID().toString().take(8)}",
                name = fileName,
                size = fileSize,
                type = AssetType.IMAGE
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
            
            val fileName = context.contentResolver.getNameFromUri(uri = uri).orEmpty()
            if (fileName.isEmpty()) failGracefully("Could not get file name")
            
            // Create project directory if it doesn't exist
            val projectDir = File(context.filesDir, "audios/$projectName")
            if (!projectDir.exists()) {
                projectDir.mkdirs()
            }
            
            // Copy file to project directory
            val destinationFile = File(projectDir, fileName)
            context.contentResolver.openInputStream(uri)!!.buffered().use { input ->
                destinationFile.outputStream().buffered().use { output ->
                    output.write(input.readBytes())
                }
            }
            
            // Get file size for display
            val fileSize = formatFileSize(destinationFile.length())
            
            val newAudio = AssetItem(
                id = "audio_${UUID.randomUUID().toString().take(8)}",
                name = fileName,
                size = fileSize,
                type = AssetType.AUDIO
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
                val imageFile = File(context.filesDir, "images/$projectName/${imageToDelete.name}")
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
                val audioFile = File(context.filesDir, "audios/$projectName/${audioToDelete.name}")
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

