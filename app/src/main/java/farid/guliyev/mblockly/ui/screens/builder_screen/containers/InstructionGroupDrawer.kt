package farid.guliyev.mblockly.ui.screens.builder_screen.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.ui.components.CustomTextFieldContainer
import farid.guliyev.mblockly.ui.components.CustomTextFieldWithValidator
import farid.guliyev.mblockly.ui.components.FloatValidator
import farid.guliyev.mblockly.ui.components.InstructionContainer
import farid.guliyev.mblockly.ui.components.button.AddInstructionButton
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlockDrawer
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionGroupMetaData
import farid.guliyev.mblockly.ui.theme.AccentEmerald
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.SurfacePrimary

@Composable
fun InstructionGroupDrawer(
    modifier: Modifier = Modifier,
    screenHeight: Dp,
    block: InstructionBlock.InstructionGroup,
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
    InstructionContainer(
        modifier = modifier,
        label = block.metaData.groupType.label,
        index = index,
        isMinimized = block.isMinimized,
        onRemove = { onRemoveInstruction(index, block) },
        onMoveUp = { onMoveUp(index, block) },
        onMoveDown = { onMoveDown(index, block) },
        onMoveOut = { onMoveOut(index, block) },
        onMoveIn =  { onMoveIn(index, block) },
        onToggleMinimize = { onUpdateInstruction(index, block.copy(isMinimized = !block.isMinimized)) },
        onEditSeparately = { onEnableFocusModeForGroup(index, block) },
        onDuplicate = { onDuplicate(index, block) },
        backgroundColor = AccentEmerald.copy(alpha = 0.1f),
        borderColor = AccentEmerald,
        innerPadding = PaddingValues(8.dp),
        content = {
            Column {
                when(val metadata = block.metaData) {
                    is InstructionGroupMetaData.Thread -> {
                        Text(
                            modifier = Modifier.padding(start = 50.dp),
                            text = block.id.take(10),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralGray700
                        )
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = SurfacePrimary.copy(0.4F)
                        )
                    }
                    InstructionGroupMetaData.InfiniteLoop -> Unit
                    is InstructionGroupMetaData.FiniteLoop -> {
                        CustomTextFieldContainer(
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Loop count",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralGray700 //if (isInputValid) NeutralGray700 else ErrorRed
                            )

                            CustomTextFieldWithValidator(
                                originalValue = metadata.loopCount,
                                onValueChange = {
                                    onUpdateInstruction(index, block.copy(metaData = metadata.copy(loopCount = it)))
                                }, validator = FloatValidator
                            )
                        }
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = SurfacePrimary.copy(0.4F)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))


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
                        onEnableFocusModeForGroup = onEnableFocusModeForGroup,
                        onDuplicate = onDuplicate
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