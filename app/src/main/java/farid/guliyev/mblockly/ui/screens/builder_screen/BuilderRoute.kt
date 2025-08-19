package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import farid.guliyev.mblockly.ui.components.sheet.AppModalBottomSheet
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import farid.guliyev.mblockly.R
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgroundedWithText
import farid.guliyev.mblockly.ui.screens.builder_screen.components.ShareBottomSheetContent
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.PrimaryBlue
import farid.guliyev.mblockly.ui.theme.SuccessGreen

@Composable
fun BuilderRoute() {

    val viewModel = remember { BuilderViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val subState by viewModel.subState.collectAsStateWithLifecycle()

    BuilderScreen(
        viewModel = viewModel,
        state = state,
        subState = subState
    )

    val sheetState by viewModel.sheetState.collectAsStateWithLifecycle()
    val context = LocalContext.current
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
                    }
                )
            }
            else -> Unit
        }
    }
}