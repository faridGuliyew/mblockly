package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.optionalFields
import farid.guliyev.mblockly.ui.components.AddInstructionButton
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawShapeBlockComponent
import farid.guliyev.mblockly.ui.screens.builder_screen.components.BuilderTopBar
import farid.guliyev.mblockly.ui.screens.builder_screen.components.TopBarMode
import farid.guliyev.mblockly.ui.screens.output_screen.OutputScreen

@Composable
fun BuilderScreen() {
    var isRunning by remember { mutableStateOf(false) }
    var topBarMode by remember { mutableStateOf(TopBarMode.HIDDEN) }
    val viewModel = remember { BuilderViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    var selectedInstructionWithIndex by remember { mutableStateOf<Pair<Instruction, Int>?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val selectedInstruction = selectedInstructionWithIndex?.first
            val selectedInstructionIndex = selectedInstructionWithIndex?.second
            BuilderTopBar(
                mode = topBarMode,
                onToggleExpand = {
                    topBarMode = if (topBarMode == TopBarMode.HIDDEN) TopBarMode.INSTRUCTION else TopBarMode.HIDDEN
                },
                onAddInstruction = { viewModel.addInstruction(it) },
                enableFieldList = selectedInstruction?.optionalFields.orEmpty(),
                onEnableField = {
                    if (selectedInstruction == null) return@BuilderTopBar

                    viewModel.editInstruction(newInstruction = selectedInstruction.enableOptionalFieldByName(it), index = selectedInstructionIndex!!)
                    selectedInstructionWithIndex = null
                },
                onExecute = { isRunning = true }
            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = state.draggableUiBlocks) { index, block ->
                // Your block
                when (val instruction = block.instruction) {
                    is Instruction.Visuals.DrawShape -> {
                        DrawShapeBlockComponent(
                            index = index,
                            instruction = instruction,
                            onEditInstruction = { viewModel.editInstruction(it, index) },
                            onRemoveInstruction = { viewModel.removeInstruction(index) },
                            onMoveUp = { viewModel.moveInstructionUp(index) },
                            onMoveDown = { viewModel.moveInstructionDown(index) },
                            onAddInstructionField = {
                                topBarMode = TopBarMode.ENABLE_FIELD
                                selectedInstructionWithIndex = instruction to index
                            }
                        )
                    }

                    is Instruction.Variables.DefineDouble -> TODO()
                    is Instruction.Variables.DefineInteger -> TODO()
                    is Instruction.Variables.DefineWord -> TODO()
                }
            }

            // Add button at the end
            item {
                AddInstructionButton(
                    onClick = { topBarMode = TopBarMode.INSTRUCTION }
                )
            }
        }
    }

    if (isRunning) {
        val instructions = remember { state.draggableUiBlocks.map { it.instruction } }
        OutputScreen(instructions = instructions, onFinish = {
            isRunning = false
        })
    }
}

@Preview
@Composable
private fun BuilderScreenPrev() {
    BuilderScreen()
}