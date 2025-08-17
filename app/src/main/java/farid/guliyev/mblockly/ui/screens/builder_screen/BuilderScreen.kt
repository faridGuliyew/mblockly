package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.optionalFields
import farid.guliyev.mblockly.ui.components.AddInstructionButton
import farid.guliyev.mblockly.ui.components.InstructionContainer
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.AnimateFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DefineFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawShapeInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.WaitInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.components.BuilderTopBar
import farid.guliyev.mblockly.ui.screens.output_screen.OutputScreen

@Composable
fun BuilderScreen() {
    var isRunning by remember { mutableStateOf(false) }
    val viewModel = remember { BuilderViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val subState by viewModel.subState.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BuilderTopBar(
                mode = subState.topBarMode,
                onHide = viewModel::hideTopBarMenu,
                onAddInstruction = viewModel::addSingleInstruction,
                enableFieldList = subState.enableOptionalFieldList,
                onEnableField = viewModel::enableInstructionOptionalField,
                onExecute = { isRunning = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            InstructionBlockDrawer(
                block = state.mainInstructionGroup,
                index = -1,
                onUpdateInstruction = viewModel::updateInstruction,
                onRemoveInstruction = viewModel::removeInstructionBlock,
                onMoveUp = viewModel::moveInstructionUp,
                onMoveDown = viewModel::moveInstructionDown,
                onMoveIn = viewModel::moveInstructionIn,
                onMoveOut = viewModel::moveInstructionOut,
                onAddInstructionGroup = viewModel::addInstructionGroup,

                onEnableFocusModeForGroup = viewModel::enableFocusEditingForGroup,
                onEnableSingleInstructionOptionalField = viewModel::showEnableSingleInstructionOptionalField,
                onAddSingleInstruction = viewModel::showAddSingleInstructionOptions
            )

            subState.focusedEditInstructionGroupsWithIndices.forEach { groupWithIndex ->
                Column(
                    modifier = Modifier.fillMaxSize().background(color = Color.White)
                        .verticalScroll(rememberScrollState())) {
                    InstructionBlockDrawer(
                        block = groupWithIndex.first.copy(isMinimized = false),
                        index = groupWithIndex.second,
                        onUpdateInstruction = viewModel::updateInstruction,
                        onRemoveInstruction = viewModel::removeInstructionBlock,
                        onMoveUp = viewModel::moveInstructionUp,
                        onMoveDown = viewModel::moveInstructionDown,
                        onMoveIn = viewModel::moveInstructionIn,
                        onMoveOut = viewModel::moveInstructionOut,
                        onAddInstructionGroup = viewModel::addInstructionGroup,
                        onEnableSingleInstructionOptionalField = viewModel::showEnableSingleInstructionOptionalField,
                        onAddSingleInstruction = viewModel::showAddSingleInstructionOptions,
                        onEnableFocusModeForGroup = { index, group ->
                            if (group.id == groupWithIndex.first.id) return@InstructionBlockDrawer

                            viewModel.enableFocusEditingForGroup(index, group)
                        }
                    )

                    Spacer(modifier = Modifier.heightIn(4.dp))
                    AddInstructionButton(
                        text = "Close `Focused editing`",
                        onClick = viewModel::exitFocusEditingForGroup
                    )
                }
            }
        }
    }


    if (isRunning) {
        val instructions = remember { state.mainInstructionGroup }
        OutputScreen(mainInstructionGroup = instructions, onFinish = {
            isRunning = false
        })
    }
}

@Composable
fun InstructionBlockDrawer(
    modifier: Modifier = Modifier,
    screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp,
    block: InstructionBlock,
    index: Int,
    onUpdateInstruction: (index: Int, InstructionBlock) -> Unit,
    onRemoveInstruction: (index: Int, InstructionBlock) -> Unit,
    onMoveUp: (index: Int, InstructionBlock) -> Unit,
    onMoveDown: (index: Int, InstructionBlock) -> Unit,
    onMoveIn: (index: Int, InstructionBlock) -> Unit,
    onMoveOut: (index: Int, InstructionBlock) -> Unit,
    onEnableSingleInstructionOptionalField: (index: Int, InstructionBlock.SingleInstruction) -> Unit,
    onEnableFocusModeForGroup: (index: Int, group: InstructionBlock.InstructionGroup) -> Unit,
    onAddInstructionGroup: (parentGroupId: String) -> Unit,
    onAddSingleInstruction: (parentGroupId: String) -> Unit,
) {
    when (block) {
        is InstructionBlock.SingleInstruction -> {
            SingleInstructionDrawer(
                instruction = block.instruction,
                index = index,
                isMinimized = block.isMinimized,
                onEditInstruction = { onUpdateInstruction(index, block.copy(instruction = it)) },
                onToggleMinimize = { onUpdateInstruction(index, block.copy(isMinimized = !block.isMinimized)) },
                onRemoveInstruction = { onRemoveInstruction(index, block) },
                onMoveUp = { onMoveUp(index, block) },
                onMoveDown = { onMoveDown(index, block) },
                onMoveOut = { onMoveOut(index, block) },
                onMoveIn =  { onMoveIn(index, block) },
                onAddInstructionField = { onEnableSingleInstructionOptionalField(index, block) }
            )
        }

        is InstructionBlock.InstructionGroup -> {
            InstructionContainer(
                modifier = modifier,
                label = "Thread: ${block.id.take(10)}",
                index = index,
                isMinimized = block.isMinimized,
                onRemove = { onRemoveInstruction(index, block) },
                onMoveUp = { onMoveUp(index, block) },
                onMoveDown = { onMoveDown(index, block) },
                onMoveOut = { onMoveOut(index, block) },
                onMoveIn =  { onMoveIn(index, block) },
                onToggleMinimize = { onUpdateInstruction(index, block.copy(isMinimized = !block.isMinimized)) },
                onEditSeparately = { onEnableFocusModeForGroup(index, block) },
                backgroundColor = Color(0xFF4CAF50),
                borderColor = Color(0xFFDEDEDE),
                innerPadding = PaddingValues(4.dp),
                content = {
                    LazyColumn (
                        modifier = Modifier.heightIn(max = screenHeight * 0.8F),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        itemsIndexed(items = block.instructionBlocks) { childIndex, childBlock ->
                            // Recursively render each child
                            InstructionBlockDrawer(
                                block = childBlock,
                                index = childIndex,
                                onUpdateInstruction = onUpdateInstruction,
                                onRemoveInstruction = onRemoveInstruction,
                                onMoveUp = onMoveUp,
                                onMoveDown = onMoveDown,
                                onMoveIn = onMoveIn,
                                onMoveOut = onMoveOut,
                                onEnableSingleInstructionOptionalField = onEnableSingleInstructionOptionalField,
                                onAddInstructionGroup = onAddInstructionGroup,
                                onAddSingleInstruction = onAddSingleInstruction,
                                onEnableFocusModeForGroup = onEnableFocusModeForGroup
                            )
                        }
                    }

                    Row {
                        AddInstructionButton(
                            modifier = Modifier.weight(1F),
                            text = "Group",
                            onClick = { onAddInstructionGroup(block.id) }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        AddInstructionButton(
                            modifier = Modifier.weight(1F),
                            text = "Block",
                            onClick = { onAddSingleInstruction(block.id) }
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun SingleInstructionDrawer(
    instruction: Instruction,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (Instruction) -> Unit,
    onRemoveInstruction: () -> Unit,
    onAddInstructionField: () -> Unit,
    onToggleMinimize: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
) {
    // Your block
    when (instruction) {
        is Instruction.Visuals.DrawShape -> {
            DrawShapeInstructionBlock(
                index = index,
                instruction = instruction,
                isMinimized = isMinimized,
                onEditInstruction = onEditInstruction,
                onToggleMinimize = onToggleMinimize,
                onRemoveInstruction = onRemoveInstruction,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
                onMoveOut = onMoveOut,
                onMoveIn = onMoveIn,
                onAddInstructionField = onAddInstructionField
            )
        }

        is Instruction.Variables.DefineFloat -> {
            DefineFloatInstructionBlock(
                index = index,
                instruction = instruction,
                isMinimized = isMinimized,
                onEditInstruction = onEditInstruction,
                onToggleMinimize = onToggleMinimize,
                onRemoveInstruction = onRemoveInstruction,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
                onMoveOut = onMoveOut,
                onMoveIn = onMoveIn,
                onAddInstructionField = onAddInstructionField
            )
        }

        is Instruction.Animations.AnimateFloat -> {
            AnimateFloatInstructionBlock(
                index = index,
                instruction = instruction,
                isMinimized = isMinimized,
                onEditInstruction = onEditInstruction,
                onToggleMinimize = onToggleMinimize,
                onRemoveInstruction = onRemoveInstruction,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
                onMoveOut = onMoveOut,
                onMoveIn = onMoveIn,
                onAddInstructionField = onAddInstructionField
            )
        }

        is Instruction.Controls.Wait -> {
            WaitInstructionBlock(
                index = index,
                instruction = instruction,
                isMinimized = isMinimized,
                onEditInstruction = onEditInstruction,
                onToggleMinimize = onToggleMinimize,
                onRemoveInstruction = onRemoveInstruction,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
                onMoveOut = onMoveOut,
                onMoveIn = onMoveIn,
                onAddInstructionField = onAddInstructionField
            )
        }

        is Instruction.Variables.DefineInteger -> TODO()
        is Instruction.Variables.DefineString -> TODO()
    }
}

@Preview
@Composable
private fun BuilderScreenPrev() {
    BuilderScreen()
}