package farid.guliyev.mblockly.ui.screens.builder_screen.containers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.AnimateFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.ChangeFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DefineFloatInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawLineInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.DrawShapeInstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.blocks.WaitInstructionBlock

@Composable
fun SingleInstructionDrawer(
    instruction: InstructionRuntime,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (InstructionRuntime) -> Unit,
    onRemoveInstruction: () -> Unit,
    onAddInstructionField: () -> Unit,
    onToggleMinimize: () -> Unit,
    onDuplicate: () -> Unit,
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
                onAddInstructionField = onAddInstructionField,
                onDuplicate = onDuplicate
            )
        }
        is InstructionRuntime.Visuals.DrawLine -> {
            DrawLineInstructionBlock(
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
                onAddInstructionField = onAddInstructionField,
                onDuplicate = onDuplicate
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
                onAddInstructionField = onAddInstructionField,
                onDuplicate = onDuplicate
            )
        }
        is InstructionRuntime.Variables.ChangeFloat -> {
            ChangeFloatInstructionBlock(
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
                onAddInstructionField = onAddInstructionField,
                onDuplicate = onDuplicate
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
                onAddInstructionField = onAddInstructionField,
                onDuplicate = onDuplicate
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
                onAddInstructionField = onAddInstructionField,
                onDuplicate = onDuplicate
            )
        }

//        is InstructionRuntime.Variables.DefineInteger -> TODO()
//        is InstructionRuntime.Variables.DefineString -> TODO()
    }
}