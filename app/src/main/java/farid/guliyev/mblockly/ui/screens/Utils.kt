package farid.guliyev.mblockly.ui.screens

fun main() {
    val name = "file_name.mb"
    val extensionRegex = """\..*$""".toRegex()
    println(extensionRegex.find(name)?.value)
}