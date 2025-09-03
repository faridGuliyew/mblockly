package farid.guliyev.mblockly.ui.screens.home_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.content_resolver.getNameFromUri
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.core.file.withoutExtension
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.ui.navigation.BuilderRoute
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel.Companion.RECENT_PROJECT_FILE_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.DateFormat
import java.util.Date


class HomeViewModel(
    private val navController: NavigationController = NavigationModule.navController
) : BaseViewModel() {

    val state = MutableStateFlow(HomeState())

    fun getImportFileIntent(): Intent {
        val importFileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            setType("*/*")
        }
        val importFileChooser = Intent.createChooser(importFileIntent, "Choose a file")
        return importFileChooser
    }

    fun importFile(context: Context, uri: Uri?) {
        runSafelyInBg {
            if (uri == null) failGracefully("No file selected")
            val fileName = context.contentResolver.getNameFromUri(uri = uri).orEmpty()

            val fileExtensionRegex = """\..*$""".toRegex()
            val fileExtension = fileExtensionRegex.find(fileName)?.value

            if (fileExtension != ".mb") failGracefully("File extension should be .mb! Please specify a valid project file.")

            context.contentResolver.openInputStream(uri)!!.buffered().use { input ->
                val fileName = showInputConfirmation("How do you want to save this file?", initialValue = fileName.withoutExtension()) + ".mb"
                val destinationFile = File(context.filesDir, fileName).also { it.createNewFile() }
                destinationFile.outputStream().buffered().use { out-> out.write(input.readBytes()) }
            }
            showSuccessAlert("Done!")
            loadProjects(context)
        }
    }

    fun loadProjects(context: Context) {
        runSafelyInBg {
            val filesDir = context.filesDir
            val formatter = DateFormat.getInstance()
            val mbFiles =
                filesDir.listFiles { file -> file.extension.lowercase() == "mb" }.orEmpty()
                    .sortedByDescending { it.lastModified() }
                    .map {
                        SavedFile(
                            name = it.name,
                            lastModified = formatter.format(Date(it.lastModified()))
                        )
                    }

            state.update { it.copy(projectFiles = mbFiles) }
        }
    }

    fun loadProjectFromFile(context: Context, fileName: String) {
        runSafelyInBg {
            val file = File(context.filesDir, fileName)
            val jsonContent = file.inputStream().buffered().readBytes().decodeToString()
            navController.sendCommand { navigate(BuilderRoute(jsonContent, fileName)) }
        }
    }

    fun deleteProjectFile(context: Context, fileName: String) {
        runSafelyInBg {
            val file = File(context.filesDir, fileName)
            file.delete()
            loadProjects(context)
        }
    }

    fun createNewProject(context: Context?) {
        runSafelyInBg {
            val file = File(context!!.filesDir, RECENT_PROJECT_FILE_NAME)
            val json = Json.encodeToString(BuilderViewModel.initialGroup)
            file.outputStream().buffered().use { it.write(json.toByteArray()) }
            navController.sendCommand { navigate(BuilderRoute(json, RECENT_PROJECT_FILE_NAME)) }
        }
    }
}