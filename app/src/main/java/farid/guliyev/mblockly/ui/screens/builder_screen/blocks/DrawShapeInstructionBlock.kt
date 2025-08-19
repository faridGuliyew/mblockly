package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.type
import farid.guliyev.mblockly.ui.components.button.EnableInstructionFieldsButton
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.components.InstructionField
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Visuals.DrawShape.OptionalFields.*
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.ui.components.OptionalInstructionField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrawShapeInstructionBlock(
    instruction: InstructionRuntime.Visuals.DrawShape,
    index: Int,
    isMinimized : Boolean,
    onEditInstruction: (InstructionRuntime.Visuals.DrawShape) -> Unit,
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
        type = instruction.base.type,
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
                instructionField = instruction.name,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instructionField = instruction.width,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instructionField = instruction.height,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instructionField = instruction.x,
                onValueChanged = onEditInstruction
            )

            InstructionField(
                instructionField = instruction.y,
                onValueChanged = onEditInstruction
            )

            /** === Optional field visibility checks === */
//            OptionalInstructionField(
//                enabledOptionalFields = instruction.base.enabledOptionalFields,
//                optionalField = CORNER_RADIUS_FIELD,
//                name = { it.name },
//                instruction = instruction.base,
//                instructionField = instruction.cornerRadius,
//                onValueChanged = onEditInstruction,
//                onDisable = onEditInstruction
//            )
//            OptionalInstructionField(
//                enabledOptionalFields = instruction.base.enabledOptionalFields,
//                optionalField = COLOR_FIELD,
//                name = { it.name },
//                instruction = instruction.base,
//                instructionField = instruction.color,
//                onValueChanged = onEditInstruction,
//                onDisable = onEditInstruction
//            )
//            OptionalInstructionField(
//                enabledOptionalFields = instruction.base.enabledOptionalFields,
//                optionalField = SCALE_FIELD,
//                name = { it.name },
//                instruction = instruction.base,
//                instructionField = instruction.scaleField,
//                onValueChanged = onEditInstruction,
//                onDisable = onEditInstruction
//            )

            /** === Optional fields add button === */
            if (instruction.base.enabledOptionalFields.size < Instruction.Visuals.DrawShape.OptionalFields.entries.size) {
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
        onMoveDown = {}, onMoveUp = {}, onAddInstructionField = {}, onToggleMinimize = {},
        isMinimized = false,
        onMoveOut = {},
        onMoveIn = {}
    )
}