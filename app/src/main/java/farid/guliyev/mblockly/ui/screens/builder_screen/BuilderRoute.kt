package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.ui.components.button.AddInstructionButton
import farid.guliyev.mblockly.ui.components.InstructionContainer
import farid.guliyev.mblockly.ui.components.sheet.AppModalBottomSheet
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.AnimateFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DefineFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawShapeInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.WaitInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.components.BuilderTopBar
import farid.guliyev.mblockly.ui.screens.output_screen.OutputScreen
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.AccentEmerald

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
    AppModalBottomSheet(
        type = sheetState,
        onDismissRequest = viewModel::hideSheet
    ) {
        when(it) {
            SheetType.SHARE -> {
                Text("SHARE!")
            }
            else -> Unit
        }
    }
}