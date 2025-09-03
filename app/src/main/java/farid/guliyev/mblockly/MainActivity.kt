package farid.guliyev.mblockly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import farid.guliyev.mblockly.di.CommsModule
import farid.guliyev.mblockly.di.CommsModule.confirmationFeedbackChannel
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.ui.components.alert.TopAlert
import farid.guliyev.mblockly.ui.components.dialog.Confirmation
import farid.guliyev.mblockly.ui.components.dialog.TopConfirmation
import farid.guliyev.mblockly.ui.navigation.BuilderRoute
import farid.guliyev.mblockly.ui.navigation.HomeRoute
import farid.guliyev.mblockly.ui.navigation.AssetsRoute
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderRoute
import farid.guliyev.mblockly.ui.screens.home_screen.HomeRoute
import farid.guliyev.mblockly.ui.screens.assets_screen.AssetsRoute
import farid.guliyev.mblockly.ui.theme.MBlocklyTheme
import kotlinx.coroutines.delay
import java.io.File

class MainActivity : ComponentActivity() {

    private val alertChannel = CommsModule.alertChannel
    private val confirmationChannel = CommsModule.confirmationChannel
    private val navigationController = NavigationModule.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MBlocklyTheme {
                val focusManager = LocalFocusManager.current
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.clickable(
                        interactionSource = null, indication = null,
                        onClick = {
                            focusManager.clearFocus()
                        }),
                    navController = navController,
                    startDestination = HomeRoute
                ) {
                    composable<HomeRoute> {
                        HomeRoute()
                    }
                    composable<BuilderRoute> {
                        BuilderRoute()
                    }
                    composable<AssetsRoute> {
                        AssetsRoute()
                    }
                }

                // Observe navigation commands
                LaunchedEffect(Unit) {
                    navigationController.observeCommands { command ->
                        navController.command()
                    }
                }

                // Show global alerts
                var visibleAlert by remember { mutableStateOf<Alert?>(null) }
                LaunchedEffect(Unit) {
                    for (alert in alertChannel) {
                        visibleAlert = alert
                    }
                }

                TopAlert(
                    alert = visibleAlert,
                    onDismiss = { visibleAlert = null }
                )

                // Show global confirmations
                var visibleConfirmation by remember { mutableStateOf<Confirmation<*>?>(null) }
                LaunchedEffect(Unit) {
                    for (confirmation in confirmationChannel) {
                        visibleConfirmation = confirmation
                    }
                }

                TopConfirmation(
                    confirmation = visibleConfirmation,
                    onDismiss = {
                        visibleConfirmation = null
                        confirmationFeedbackChannel.trySend(null)
                    },
                    onConfirm = {
                        visibleConfirmation = null
                        confirmationFeedbackChannel.trySend(it)
                    }
                )
            }
        }
    }
}