package farid.guliyev.mblockly.ui.screens.output_screen.media

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import farid.guliyev.mblockly.MyFileProvider
import farid.guliyev.mblockly.utils.ProjectPathUtils
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

suspend fun playSound(context: Context, fileName: String, projectName: String? = null) {
    // Try to find the audio file in the project-specific audio directory
    val audioFile = ProjectPathUtils.getProjectAudioFile(context.filesDir, projectName!!, fileName)
    
    // Check if the file exists
    if (!audioFile.exists()) {
        throw IllegalArgumentException("Audio file '$fileName' not found in project assets")
    }

    val asset = MyFileProvider.getUriForFile(context, audioFile)

    val mediaPlayer = MediaPlayer()
    try {
        mediaPlayer.setDataSource(context, asset)
        mediaPlayer.prepare()
        mediaPlayer.start()
        // Blocking wait until finished
        while (mediaPlayer.isPlaying) {
            delay(100.milliseconds)
        }
    } finally {
        mediaPlayer.release()
    }
}