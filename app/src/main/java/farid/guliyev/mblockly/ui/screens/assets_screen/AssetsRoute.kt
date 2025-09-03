package farid.guliyev.mblockly.ui.screens.assets_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AssetsRoute() {
    val context = LocalContext.current
    val viewModel = viewModel<AssetsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Load existing assets when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadProjectAssets(context)
    }

    AssetsScreen(
        viewModel = viewModel,
        state = state
    )
}

