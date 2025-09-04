package farid.guliyev.mblockly.core.compression

import android.R.attr.path
import android.content.Context
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


private const val BUFFER = 2048

/**
 * Compresses a file into a zip file
 * @param file The source file
 * @param target The target file
 *
 * @throws NullPointerException If the entry name is null
 * @throws IllegalArgumentException If the entry name is longer than 0xFFFF byte
 * @throws SecurityException If a security manager exists and its SecurityManager.checkRead(String)
 * method denies read access to the file
 * @throws ZipException If a ZIP format error has occurred
 * @throws IOException If an I/O error has occurred
 */
@Throws(
    NullPointerException::class,
    IllegalArgumentException::class,
    SecurityException::class,
    ZipException::class,
    IOException::class
)
fun Context.zipFile(file: File): File {
    var zipOutput: ZipOutputStream? = null
    val target = File(cacheDir, "${file.name}.zip").also { if (it.exists()) it.delete() }
    try {
        zipOutput = ZipOutputStream(target.outputStream().buffered(BUFFER))
        if (file.isDirectory)
            addFolderToZip(zipOutput, file)
        else {
            addFileToZip(zipOutput, file)
        }
    } finally {
        zipOutput?.close()
    }

    return target
}

private fun Context.addFolderToZip(zipOutput: ZipOutputStream, folder: File) {
    val files = folder.listFiles() ?: return

    for (file in files) {
        when (file.isDirectory) {
            true -> addFolderToZip(zipOutput, file)
            false -> addFileToZip(zipOutput, file)
        }
    }
}

private fun Context.addFileToZip(zipOutput: ZipOutputStream, file: File) {
    val data = ByteArray(BUFFER)
    val origin = file.inputStream().buffered(BUFFER)
    val relativePath = file.absolutePath.replace(""".*${packageName}/files/""".toRegex(), "")
    val entry = ZipEntry(relativePath).also { it.time = file.lastModified() }
    zipOutput.putNextEntry(entry)

    var count = origin.read(data, 0, BUFFER)
    while (count != -1) {
        zipOutput.write(data, 0, count)
        count = origin.read(data, 0, BUFFER)
    }
}


fun Context.unzipFile(stream: BufferedInputStream, oldName: String, newName: String): Boolean {
    try {
        val zipInputStream = ZipInputStream(stream)
        var zipEntry: ZipEntry

        val buffer = ByteArray(BUFFER)
        var count: Int

        while ((zipInputStream.getNextEntry().also { zipEntry = it }) != null) {
            val file = File(filesDir,zipEntry.name.replace(oldName, newName))

            if (zipEntry.isDirectory) {
                file.also { it.mkdirs() }
                continue
            }

            file.parentFile?.mkdirs() // Need to create directories if not exists
            FileOutputStream(file).use {
                while ((zipInputStream.read(buffer).also { count = it }) != -1) {
                    it.write(buffer, 0, count)
                }
                zipInputStream.closeEntry()
            }
        }

        zipInputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }

    return true
}