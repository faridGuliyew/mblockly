package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Visuals.DrawShape.OptionalFields.COLOR_FIELD
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Visuals.DrawShape.OptionalFields.CORNER_RADIUS_FIELD
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Visuals.DrawShape.OptionalFields.SCALE_FIELD
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.domain.model.instruction.type
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.components.InstructionField
import farid.guliyev.mblockly.ui.components.OptionalInstructionField
import farid.guliyev.mblockly.ui.components.button.EnableInstructionFieldsButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChangeFloatInstructionBlock(
    instruction: InstructionRuntime.Variables.ChangeFloat,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (InstructionRuntime.Variables.ChangeFloat) -> Unit,
    onRemoveInstruction: () -> Unit,
    onAddInstructionField: () -> Unit,
    onToggleMinimize: () -> Unit,
    onDuplicate: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
) {
    SingleInstructionContainer (
        index = index,
        type = instruction.base.type,
        onRemove = onRemoveInstruction,
        onMoveDown = onMoveDown,
        onMoveUp = onMoveUp,
        isMinimized = isMinimized,
        onToggleMinimize = onToggleMinimize,
        onMoveOut = onMoveOut,
        onMoveIn = onMoveIn,
        onDuplicate = onDuplicate
    ) {
        // Inputs row
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InstructionField(
                instructionField = instruction.nameField,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instructionField = instruction.deltaField,
                onValueChanged = onEditInstruction
            )

            /** === Optional field visibility checks === */
            OptionalInstructionField(
                enabledOptionalFields = instruction.base.enabledOptionalFields,
                optionalField = Instruction.Variables.ChangeFloat.OptionalFields.DURATION_FIELD,
                name = { it.name },
                instruction = instruction,
                instructionField = instruction.durationField,
                onValueChanged = onEditInstruction,
                onDisable = onEditInstruction
            )
            /** === Optional fields add button === */
            if (instruction.base.enabledOptionalFields.size < Instruction.Variables.ChangeFloat.OptionalFields.entries.size) {
                EnableInstructionFieldsButton(onClick = onAddInstructionField)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawShapeBlockComponentPrev() {
    DrawShapeInstructionBlock(
        instruction = InstructionRuntime.Visuals.DrawShape(Instruction.Visuals.DrawShape()),
        index = 0,
        onEditInstruction = {},
        onRemoveInstruction = {},
        onMoveDown = {}, onMoveUp = {}, onAddInstructionField = {}, onToggleMinimize = {},isMinimized = false,
        onMoveOut = {}, onMoveIn = {}, onDuplicate = {}
    )
}