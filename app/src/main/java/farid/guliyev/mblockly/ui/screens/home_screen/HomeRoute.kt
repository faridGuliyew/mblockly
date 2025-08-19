package farid.guliyev.mblockly.ui.screens.home_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeRoute() {
    val homeViewModel = viewModel<HomeViewModel>()
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        viewModel = homeViewModel,
        state = state
    )
}