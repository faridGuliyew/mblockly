package farid.guliyev.mblockly.di

import androidx.navigation.NavHostController
import kotlinx.coroutines.channels.Channel

object NavigationModule {
    val navController = NavigationController()
}


class NavigationController {
    private val navigationChannel = Channel<NavHostController.() -> Unit>()

    suspend fun observeCommands(onCommand: (NavHostController.() -> Unit) -> Unit) {
        for (command in navigationChannel) {
            onCommand(command)
        }
    }

    fun sendCommand(command: NavHostController.() -> Unit) {
        navigationChannel.trySend(command)
    }
}