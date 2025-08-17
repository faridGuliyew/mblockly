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
import farid.guliyev.mblockly.di.CommsModule
import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.ui.components.alert.TopAlert
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderScreen
import farid.guliyev.mblockly.ui.theme.MBlocklyTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val alertChannel = CommsModule.alertChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MBlocklyTheme {
                val focusManager = LocalFocusManager.current
                Box(
                    modifier = Modifier.clickable(
                        interactionSource = null, indication = null,
                        onClick = {
                            focusManager.clearFocus()
                        })
                ) {
                    BuilderScreen()
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
            }
        }
    }
}