package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.type
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.components.InstructionField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefineFloatInstructionBlock(
    instruction: Instruction.Variables.DefineFloat,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (Instruction.Variables.DefineFloat) -> Unit,
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
        type = instruction.type,
        onRemove = onRemoveInstruction,
        onMoveDown = onMoveDown,
        onMoveUp = onMoveUp,
        isMinimized = isMinimized,
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
                instructionField = instruction.nameField,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instruction = instruction,
                instructionField = instruction.valueField,
                onValueChanged = onEditInstruction
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawShapeBlockComponentPrev() {
    DrawShapeInstructionBlock(
        instruction = Instruction.Visuals.DrawShape(),
        index = 0,
        onEditInstruction = {},
        onRemoveInstruction = {},
        onMoveDown = {}, onMoveUp = {}, onAddInstructionField = {}, onToggleMinimize = {},isMinimized = false,
        onMoveOut = {}, onMoveIn = {}
    )
}