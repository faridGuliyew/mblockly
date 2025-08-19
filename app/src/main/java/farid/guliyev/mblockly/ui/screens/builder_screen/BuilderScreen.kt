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
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.ui.components.button.AddInstructionButton
import farid.guliyev.mblockly.ui.components.InstructionContainer
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.AnimateFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DefineFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawShapeInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.WaitInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.components.BuilderTopBar
import farid.guliyev.mblockly.ui.screens.output_screen.OutputScreen
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.AccentEmerald

@Composable
fun BuilderScreen(
    viewModel: BuilderViewModel, // Methods are called directly, for performance reasons (otherwise lambas are unstable)
    state: BuilderState,
    subState: BuilderSubState
) {
    var isRunning by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BuilderTopBar(
                mode = subState.topBarMode,
                onHide = viewModel::hideTopBarMenu,
                onAddInstruction = viewModel::addSingleInstruction,
                enableFieldList = subState.enableOptionalFieldList,
                onEnableField = viewModel::enableInstructionOptionalField,
                onShare = viewModel::showShareSheet,
                onBack = viewModel::goBack,
                onExecute = { isRunning = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
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
                    modifier = Modifier.fillMaxSize().background(color = BackgroundPrimary)
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

                    Spacer(modifier = Modifier.heightIn(8.dp))
                    AddInstructionButton(
                        text = "Close `Focused editing`",
                        backgroundColor = AccentEmerald,
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
                backgroundColor = AccentEmerald.copy(alpha = 0.1f),
                borderColor = AccentEmerald,
                innerPadding = PaddingValues(8.dp),
                content = {
                    LazyColumn (
                        modifier = Modifier.heightIn(max = screenHeight * 0.8F),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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

                        item {
                            Row {
                                AddInstructionButton(
                                    modifier = Modifier.weight(1F),
                                    text = "Group",
                                    onClick = { onAddInstructionGroup(block.id) }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                AddInstructionButton(
                                    modifier = Modifier.weight(1F),
                                    text = "Block",
                                    onClick = { onAddSingleInstruction(block.id) }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun SingleInstructionDrawer(
    instruction: InstructionRuntime,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (InstructionRuntime) -> Unit,
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
        is InstructionRuntime.Visuals.DrawShape -> {
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

        is InstructionRuntime.Variables.DefineFloat -> {
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

        is InstructionRuntime.Animations.AnimateFloat -> {
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

        is InstructionRuntime.Controls.Wait -> {
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

//        is InstructionRuntime.Variables.DefineInteger -> TODO()
//        is InstructionRuntime.Variables.DefineString -> TODO()
    }
}

@Preview
@Composable
private fun BuilderScreenPrev() {
//    BuilderScreen(BuilderViewModel(), BuilderState(), BuilderSubState())
}