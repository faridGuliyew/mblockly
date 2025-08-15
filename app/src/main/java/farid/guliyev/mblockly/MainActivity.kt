package farid.guliyev.mblockly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderScreen
import farid.guliyev.mblockly.ui.screens.builder_screen.components.BuilderTopBar
import farid.guliyev.mblockly.ui.theme.MBlocklyTheme

class MainActivity : ComponentActivity() {
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
            }
        }
    }
}