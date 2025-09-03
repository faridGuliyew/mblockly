package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.ui.components.button.AddInstructionButton
import farid.guliyev.mblockly.ui.screens.builder_screen.components.BuilderTopBar
import farid.guliyev.mblockly.ui.screens.builder_screen.containers.InstructionGroupDrawer
import farid.guliyev.mblockly.ui.screens.builder_screen.containers.SingleInstructionDrawer
import farid.guliyev.mblockly.ui.screens.output_screen.OutputScreen
import farid.guliyev.mblockly.ui.theme.AccentEmerald
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BuilderScreen(
    viewModel: BuilderViewModel, // Methods are called directly, for performance reasons (otherwise lambas are unstable)
    state: BuilderState,
    subState: BuilderSubState
) {
    val scope = rememberCoroutineScope()
    var isRunning by remember { mutableStateOf(false) }
    var splitScreenWeight by remember { mutableFloatStateOf(0.5F) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BuilderTopBar(
                mode = subState.topBarMode,
                projectName = subState.projectName,
                onHide = viewModel::hideTopBarMenu,
                onAddSingleInstruction = viewModel::addSingleInstruction,
                onAddInstructionGroup = viewModel::addInstructionGroup,
                enableFieldList = subState.enableOptionalFieldList,
                onEnableField = viewModel::enableInstructionOptionalField,
                onShare = viewModel::showShareSheet,
                onBack = viewModel::goBack,
                onOpenAssets = viewModel::onOpenAssets,
                onExecute = {
                    scope.launch {
                        isRunning = false
                        delay(50)
                        isRunning = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1F - splitScreenWeight)
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
                    onAddInstructionGroup = viewModel::showAddInstructionGroupOptions,
                    onDuplicate = viewModel::duplicateInstruction,

                    onEnableFocusModeForGroup = viewModel::enableFocusEditingForGroup,
                    onEnableSingleInstructionOptionalField = viewModel::showEnableSingleInstructionOptionalField,
                    onAddSingleInstruction = viewModel::showAddSingleInstructionOptions
                )

                subState.focusedEditInstructionGroupsWithIndices.forEach { groupWithIndex ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = BackgroundPrimary)
                            .verticalScroll(rememberScrollState())
                    ) {
                        InstructionBlockDrawer(
                            block = groupWithIndex.first.copy(isMinimized = false),
                            index = groupWithIndex.second,
                            onUpdateInstruction = viewModel::updateInstruction,
                            onRemoveInstruction = viewModel::removeInstructionBlock,
                            onMoveUp = viewModel::moveInstructionUp,
                            onMoveDown = viewModel::moveInstructionDown,
                            onMoveIn = viewModel::moveInstructionIn,
                            onMoveOut = viewModel::moveInstructionOut,
                            onAddInstructionGroup = viewModel::showAddInstructionGroupOptions,
                            onEnableSingleInstructionOptionalField = viewModel::showEnableSingleInstructionOptionalField,
                            onAddSingleInstruction = viewModel::showAddSingleInstructionOptions,
                            onDuplicate = viewModel::duplicateInstruction,
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

            if (isRunning) {
                val instructions = remember { state.mainInstructionGroup }
                OutputScreen(
                    mainInstructionGroup = instructions,
                    currentDrag = splitScreenWeight,
                    onDrag = { splitScreenWeight = it },
                    onFinish = {
                        isRunning = false
                    },
                    getParent = {
                        with(viewModel) { it.getParent() }
                    },
                    onError = {
                        viewModel.showErrorAlert(it)
                        isRunning = false
                    },
                    projectName = subState.projectName
                )
            }
        }
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
    onDuplicate: (index: Int, InstructionBlock) -> Unit
) {
    when (block) {
        is InstructionBlock.SingleInstruction -> {
            SingleInstructionDrawer(
                instruction = block.instruction,
                index = index,
                isMinimized = block.isMinimized,
                isActive = block.isActive,
                onEditInstruction = { onUpdateInstruction(index, block.copy(instruction = it)) },
                onToggleMinimize = { onUpdateInstruction(index, block.copy(isMinimized = !block.isMinimized)) },
                onToggleActive = { onUpdateInstruction(index, block.copy(isActive = !block.isActive)) },
                onRemoveInstruction = { onRemoveInstruction(index, block) },
                onMoveUp = { onMoveUp(index, block) },
                onMoveDown = { onMoveDown(index, block) },
                onMoveOut = { onMoveOut(index, block) },
                onMoveIn = { onMoveIn(index, block) },
                onDuplicate = { onDuplicate(index, block) },
                onAddInstructionField = { onEnableSingleInstructionOptionalField(index, block) }
            )
        }

        is InstructionBlock.InstructionGroup -> {
            InstructionGroupDrawer(
                modifier = modifier,
                index = index,
                screenHeight = screenHeight,
                block = block,
                onMoveUp = onMoveUp,
                onUpdateInstruction = onUpdateInstruction,
                onRemoveInstruction = onRemoveInstruction,
                onMoveDown = onMoveDown,
                onMoveIn = onMoveIn,
                onMoveOut = onMoveOut,
                onEnableSingleInstructionOptionalField = onEnableSingleInstructionOptionalField,
                onEnableFocusModeForGroup = onEnableFocusModeForGroup,
                onAddInstructionGroup = onAddInstructionGroup,
                onAddSingleInstruction = onAddSingleInstruction,
                onDuplicate = onDuplicate
            )
        }
    }
}

@Preview
@Composable
private fun BuilderScreenPrev() {
//    BuilderScreen(BuilderViewModel(), BuilderState(), BuilderSubState())
}