package farid.guliyev.mblockly.ui.screens.home_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.compression.unzipFile
import farid.guliyev.mblockly.core.content_resolver.getNameFromUri
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.core.file.withoutExtension
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.ui.navigation.BuilderRoute
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel.Companion.RECENT_PROJECT_NAME
import farid.guliyev.mblockly.utils.createNewProject
import farid.guliyev.mblockly.utils.deleteProject
import farid.guliyev.mblockly.utils.getProjectDir
import farid.guliyev.mblockly.utils.getProjectMBFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.DateFormat
import java.util.Date


class HomeViewModel(
    private val navController: NavigationController = NavigationModule.navController
) : BaseViewModel() {

    val state = MutableStateFlow(HomeState())

    fun getImportFileIntent(): Intent {
        val importFileIntent = Intent(Intent.ACTION_GET_CONTENT).apply { setType("application/zip") }
        val importFileChooser = Intent.createChooser(importFileIntent, "Choose a file")
        return importFileChooser
    }

    fun importProject(context: Context, uri: Uri?) {
        runSafelyInBg {
            if (uri == null) failGracefully("No file selected")
            val originalProjectName = context.contentResolver.getNameFromUri(uri = uri).orEmpty().withoutExtension()

            context.contentResolver.openInputStream(uri)!!.buffered().use { input ->
                val projectName = showInputConfirmation("How do you want to save this project?", initialValue = originalProjectName)
                val destinationFile = context.getProjectDir(projectName)
                if (destinationFile.exists()) failGracefully("Project named $projectName already exists!")

                context.unzipFile(input, originalProjectName, projectName)
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
                filesDir.listFiles { file -> file.isDirectory }.orEmpty()
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

    fun loadProjectFromMBFile(context: Context, projectName: String) {
        runSafelyInBg {
            val file = context.getProjectMBFile(projectName)
            val jsonContent = file.inputStream().buffered().readBytes().decodeToString()
            navController.sendCommand { navigate(BuilderRoute(jsonContent, projectName)) }
        }
    }

    fun deleteProject(context: Context, projectName: String) {
        runSafelyInBg {
            context.deleteProject(projectName)
            loadProjects(context)
        }
    }

    fun createNewProject(context: Context?) {
        runSafelyInBg {
            val file = try {
                context!!.createNewProject(RECENT_PROJECT_NAME)
            } catch (_: Exception) {
                val newProjectName = showInputConfirmation("Enter a new name, since $RECENT_PROJECT_NAME already exists.", "project_name")
                context!!.createNewProject(newProjectName)
            }

            val json = Json.encodeToString(BuilderViewModel.initialGroup)
            file.outputStream().buffered().use { it.write(json.toByteArray()) }

            navController.sendCommand { navigate(BuilderRoute(json, file.name)) }
        }
    }
}