package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.type
import farid.guliyev.mblockly.ui.components.button.EnableInstructionFieldsButton
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.components.InstructionField
import farid.guliyev.mblockly.domain.model.Instruction.Visuals.DrawShape.OptionalFields.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrawShapeInstructionBlock(
    instruction: Instruction.Visuals.DrawShape,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (Instruction.Visuals.DrawShape) -> Unit,
    onToggleMinimize: () -> Unit,
    onRemoveInstruction: () -> Unit,
    onAddInstructionField: () -> Unit,
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
        onMoveOut = onMoveOut,
        onMoveIn = onMoveIn,
        isMinimized = isMinimized,
        onToggleMinimize = onToggleMinimize
    ) {
        // Inputs row
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InstructionField(
                instruction = instruction,
                instructionField = instruction.name,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instruction = instruction,
                instructionField = instruction.width,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instruction = instruction,
                instructionField = instruction.height,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instruction = instruction,
                instructionField = instruction.x,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instruction = instruction,
                instructionField = instruction.y,
                onValueChanged = onEditInstruction
            )

            /** === Optional field visibility checks === */
            if (instruction.enabledOptionalFields.contains(CORNER_RADIUS_FIELD)) {
                InstructionField(
                    instruction = instruction,
                    instructionField = instruction.cornerRadius,
                    onValueChanged = onEditInstruction
                )
            }
            if (instruction.enabledOptionalFields.contains(COLOR_FIELD)) {
                InstructionField(
                    instruction = instruction,
                    instructionField = instruction.color,
                    onValueChanged = onEditInstruction
                )
            }
            if (instruction.enabledOptionalFields.contains(SCALE_FIELD)) {
                InstructionField(
                    instruction = instruction,
                    instructionField = instruction.scaleField,
                    onValueChanged = onEditInstruction
                )
            }

            /** === Optional fields add button === */
            if (instruction.enabledOptionalFields.size < Instruction.Visuals.DrawShape.OptionalFields.entries.size) {
                EnableInstructionFieldsButton(onClick = onAddInstructionField)
            }
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
        onMoveDown = {}, onMoveUp = {}, onAddInstructionField = {}, onToggleMinimize = {},
        isMinimized = false,
        onMoveOut = {},
        onMoveIn = {}
    )
}