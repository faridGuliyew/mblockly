package farid.guliyev.mblockly.ui.screens.builder_screen

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import farid.guliyev.mblockly.ui.components.sheet.AppModalBottomSheet
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import farid.guliyev.mblockly.ui.screens.builder_screen.components.ShareBottomSheetContent
import java.io.File

@Composable
fun BuilderRoute() {

    val context = LocalContext.current
    val viewModel = viewModel<BuilderViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val subState by viewModel.subState.collectAsStateWithLifecycle()

    BackHandler { viewModel.goBack(context) }
    BuilderScreen(
        viewModel = viewModel,
        state = state,
        subState = subState
    )

    val sheetState by viewModel.sheetState.collectAsStateWithLifecycle()
    AppModalBottomSheet(
        type = sheetState,
        onDismissRequest = viewModel::hideSheet
    ) {
        when(it) {
            SheetType.SHARE -> {
                ShareBottomSheetContent(
                    onDismiss = viewModel::hideSheet,
                    onSave = {
                        viewModel.saveToFile(context, it)
                    },
                    onShare = {
                        viewModel.shareFile(context)
                    }
                )
            }
            else -> Unit
        }
    }
}