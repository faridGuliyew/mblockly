package farid.guliyev.mblockly.ui.screens.output_screen.media

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import farid.guliyev.mblockly.MyFileProvider
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

suspend fun playSound(context: Context, fileName: String) {
    val asset = MyFileProvider.getUriForFile(context, File(context.filesDir, fileName))

    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource(context, asset)
    mediaPlayer.prepare()
    mediaPlayer.start()

    // Blocking wait until finished
    while (mediaPlayer.isPlaying) {
        delay(100.milliseconds)
    }

    mediaPlayer.release()
}