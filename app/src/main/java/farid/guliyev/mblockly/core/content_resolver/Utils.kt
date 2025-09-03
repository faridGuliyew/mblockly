package farid.guliyev.mblockly.core.content_resolver

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

fun ContentResolver.getNameFromUri(uri: Uri): String? {
    val returnCursor = query(uri, null, null, null, null) ?: return null
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}