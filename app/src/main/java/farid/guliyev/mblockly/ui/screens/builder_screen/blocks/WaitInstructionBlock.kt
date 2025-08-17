package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.type
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.components.InstructionField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WaitInstructionBlock(
    instruction: Instruction.Controls.Wait,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (Instruction.Controls.Wait) -> Unit,
    onRemoveInstruction: () -> Unit,
    onAddInstructionField: () -> Unit,
    onToggleMinimize: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
) {
    SingleInstructionContainer (
        index = index,
        isMinimized = isMinimized,
        type = instruction.type,
        onRemove = onRemoveInstruction,
        onMoveDown = onMoveDown,
        onMoveUp = onMoveUp,
        onToggleMinimize = onToggleMinimize,
        onMoveOut = onMoveOut,
        onMoveIn = onMoveIn,
    ) {
        // Inputs row
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InstructionField(
                instruction = instruction,
                instructionField = instruction.durationField,
                onValueChanged = onEditInstruction
            )
        }
    }
}