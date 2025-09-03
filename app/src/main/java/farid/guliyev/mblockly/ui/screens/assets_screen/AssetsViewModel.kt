package farid.guliyev.mblockly.ui.screens.assets_screen

import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class AssetsViewModel : BaseViewModel() {
    
    val state = MutableStateFlow(AssetsState())
    private val navigationController: NavigationController = NavigationModule.navController
    
    init {
        loadMockData()
    }
    
    private fun loadMockData() {
        val mockImages = listOf(
            AssetItem("img_1", "background.jpg", "2.3 MB", "Image"),
            AssetItem("img_2", "logo.png", "156 KB", "Image"),
            AssetItem("img_3", "icon.svg", "45 KB", "Image")
        )
        
        val mockAudioFiles = listOf(
            AssetItem("audio_1", "background_music.mp3", "4.7 MB", "Audio"),
            AssetItem("audio_2", "sound_effect.wav", "892 KB", "Audio"),
            AssetItem("audio_3", "notification.mp3", "234 KB", "Audio")
        )
        
        state.update { 
            it.copy(
                images = mockImages,
                audioFiles = mockAudioFiles
            )
        }
    }
    
    fun uploadImage() {
        runSafelyInBg {
            // Mock upload - in real implementation, this would handle file picker and upload
            val newImage = AssetItem(
                id = "img_${UUID.randomUUID().toString().take(8)}",
                name = "uploaded_image_${System.currentTimeMillis()}.jpg",
                size = "1.2 MB",
                type = "Image"
            )
            
            state.update { 
                it.copy(images = it.images + newImage)
            }
            
            showSuccessAlert("Image uploaded successfully!")
        }
    }
    
    fun uploadAudio() {
        runSafelyInBg {
            // Mock upload - in real implementation, this would handle file picker and upload
            val newAudio = AssetItem(
                id = "audio_${UUID.randomUUID().toString().take(8)}",
                name = "uploaded_audio_${System.currentTimeMillis()}.mp3",
                size = "3.1 MB",
                type = "Audio"
            )
            
            state.update { 
                it.copy(audioFiles = it.audioFiles + newAudio)
            }
            
            showSuccessAlert("Audio file uploaded successfully!")
        }
    }
    
    fun deleteImage(imageId: String) {
        runSafelyInBg {
            state.update { 
                it.copy(images = it.images.filter { image -> image.id != imageId })
            }
            showSuccessAlert("Image deleted successfully!")
        }
    }
    
    fun deleteAudio(audioId: String) {
        runSafelyInBg {
            state.update { 
                it.copy(audioFiles = it.audioFiles.filter { audio -> audio.id != audioId })
            }
            showSuccessAlert("Audio file deleted successfully!")
        }
    }

    fun goBack() {
        navigationController.sendCommand { popBackStack() }
    }
}

