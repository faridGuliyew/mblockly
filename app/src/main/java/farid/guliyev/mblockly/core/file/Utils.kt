package farid.guliyev.mblockly.core.file

fun String.withoutExtension() : String {
    val fileExtensionRegex = """\..*$""".toRegex()
    return this.replace(fileExtensionRegex, "")
}