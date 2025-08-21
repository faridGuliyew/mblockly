package farid.guliyev.mblockly.ui.screens.home_screen

import android.content.Context
import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.ui.navigation.BuilderRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.text.DateFormat
import java.util.Date

class HomeViewModel (
    private val navController: NavigationController = NavigationModule.navController
) : BaseViewModel() {

    val state = MutableStateFlow(HomeState())

    fun importProject(context: Context) {
        runSafelyInBg {
            failGracefully("Not yet supported")
        }
    }

    fun loadProjects(context: Context) {
        runSafelyInBg {
            val filesDir = context.filesDir
            val formatter = DateFormat.getInstance()
            val mbFiles = filesDir.listFiles { file -> file.extension.lowercase() == "mb" }.orEmpty()
                .sortedByDescending { it.lastModified() }
                .map { SavedFile(name = it.name, lastModified = formatter.format(Date(it.lastModified()))) }

            state.update { it.copy(projectFiles = mbFiles) }
        }
    }

    fun loadProjectFromFile(context: Context, fileName: String) {
        runSafelyInBg {
            val file = File(context.filesDir, fileName)
            val jsonContent = file.inputStream().buffered().readBytes().decodeToString()
            navController.sendCommand { navigate(BuilderRoute(jsonContent)) }
        }
    }

    fun clearProjects() {
        state.update { it.copy(projectFiles = emptyList()) }
    }

    fun goToBuilderScreen() {
        navController.sendCommand { navigate(BuilderRoute()) }
    }
}