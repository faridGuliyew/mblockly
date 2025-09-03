package farid.guliyev.mblockly.ui.screens.builder_screen.containers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.domain.model.instruction.type
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.AnimateFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.ChangeFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DefineFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawImageInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawLineInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawShapeInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawTextInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.PlaySoundInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.WaitInstructionBlock

@Composable
fun SingleInstructionDrawer(
    instruction: InstructionRuntime,
    index: Int,
    isMinimized : Boolean,
    isActive : Boolean,
    onEditInstruction: (InstructionRuntime) -> Unit,
    onRemoveInstruction: () -> Unit,
    onAddInstructionField: () -> Unit,
    onToggleMinimize: () -> Unit,
    onToggleActive: () -> Unit,
    onDuplicate: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
) {
    SingleInstructionContainer(
        index = index,
        type = instruction.base.type,
        onRemove = onRemoveInstruction,
        onMoveDown = onMoveDown,
        onMoveUp = onMoveUp,
        onMoveOut = onMoveOut,
        onMoveIn = onMoveIn,
        isMinimized = isMinimized,
        isActive = isActive,
        onToggleMinimize = onToggleMinimize,
        onToggleActive = onToggleActive,
        onDuplicate = onDuplicate
    ) {
        // Your block content
        when (instruction) {
            is InstructionRuntime.Visuals -> {
                when (instruction) {
                    is InstructionRuntime.Visuals.DrawShape -> {
                        DrawShapeInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction,
                            onAddInstructionField = onAddInstructionField
                        )
                    }
                    is InstructionRuntime.Visuals.DrawLine -> {
                        DrawLineInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction,
                            onAddInstructionField = onAddInstructionField
                        )
                    }
                    is InstructionRuntime.Visuals.DrawText -> {
                        DrawTextInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction,
                            onAddInstructionField = onAddInstructionField
                        )
                    }
                    is InstructionRuntime.Visuals.DrawImage -> {
                        DrawImageInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction,
                            onAddInstructionField = onAddInstructionField
                        )
                    }
                }
            }
            is InstructionRuntime.Variables -> {
                when (instruction) {
                    is InstructionRuntime.Variables.DefineFloat -> {
                        DefineFloatInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction
                        )
                    }
                    is InstructionRuntime.Variables.ChangeFloat -> {
                        ChangeFloatInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction,
                            onAddInstructionField = onAddInstructionField
                        )
                    }
                }
            }
            is InstructionRuntime.Animations -> {
                when (instruction) {
                    is InstructionRuntime.Animations.AnimateFloat -> {
                        AnimateFloatInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction
                        )
                    }
                }
            }

            is InstructionRuntime.Controls -> {
                when (instruction) {
                    is InstructionRuntime.Controls.Wait -> {
                        WaitInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction
                        )
                    }
                }
            }

            is InstructionRuntime.Media -> {
                when (instruction) {
                    is InstructionRuntime.Media.PlaySound -> {
                        PlaySoundInstructionBlock(
                            instruction = instruction,
                            onEditInstruction = onEditInstruction,
                            onAddInstructionField = onAddInstructionField
                        )
                    }
                }
            }
        }
    }
}