package farid.guliyev.mblockly;
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class MyFileProvider : FileProvider() {
    companion object {
        fun getUriForFile(context: Context, file: File) : Uri {
            return getUriForFile(context, "${context.packageName}.provider", file)
        }
    }
}