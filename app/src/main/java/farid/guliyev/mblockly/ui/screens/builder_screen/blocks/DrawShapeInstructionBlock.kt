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
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Visuals.DrawShape.OptionalFields.ROTATION
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Visuals.DrawShape.OptionalFields.SCALE_FIELD
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.ui.components.InstructionField
import farid.guliyev.mblockly.ui.components.OptionalInstructionField
import farid.guliyev.mblockly.ui.components.button.EnableInstructionFieldsButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrawShapeInstructionBlock(
    instruction: InstructionRuntime.Visuals.DrawShape,
    onEditInstruction: (InstructionRuntime.Visuals.DrawShape) -> Unit,
    onAddInstructionField: () -> Unit
) {
    // Inputs row
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = Instruction.Visuals.DrawShape.OptionalFields.NAME_FIELD,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.name,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = CORNER_RADIUS_FIELD,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.cornerRadius,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = ROTATION,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.rotation,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = COLOR_FIELD,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.color,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = SCALE_FIELD,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.scaleField,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )

        /** === Optional fields add button === */
        if (instruction.base.enabledOptionalFields.size < Instruction.Visuals.DrawShape.OptionalFields.entries.size) {
            EnableInstructionFieldsButton(onClick = onAddInstructionField)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawShapeBlockComponentPrev() {
    DrawShapeInstructionBlock(
        instruction = InstructionRuntime.Visuals.DrawShape(Instruction.Visuals.DrawShape()),
        onEditInstruction = {},
        onAddInstructionField = {}
    )
}