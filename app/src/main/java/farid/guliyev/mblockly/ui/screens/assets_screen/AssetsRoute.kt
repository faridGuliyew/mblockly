package farid.guliyev.mblockly.ui.screens.assets_screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule

@Composable
fun AssetsRoute() {
    val viewModel = viewModel<AssetsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AssetsScreen(
        viewModel = viewModel,
        state = state
    )
}

