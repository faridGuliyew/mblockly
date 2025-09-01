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
fun DrawLineInstructionBlock(
    instruction: InstructionRuntime.Visuals.DrawLine,
    onEditInstruction: (InstructionRuntime.Visuals.DrawLine) -> Unit,
    onAddInstructionField: () -> Unit
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InstructionField(
            instructionField = instruction.startX,
            onValueChanged = onEditInstruction
        )

        InstructionField(
            instructionField = instruction.startY,
            onValueChanged = onEditInstruction
        )

        InstructionField(
            instructionField = instruction.endX,
            onValueChanged = onEditInstruction
        )

        InstructionField(
            instructionField = instruction.endY,
            onValueChanged = onEditInstruction
        )

        // === Optional fields ===
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = Instruction.Visuals.DrawLine.OptionalFields.THICKNESS_FIELD,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.thickness,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )
        OptionalInstructionField(
            enabledOptionalFields = instruction.base.enabledOptionalFields,
            optionalField = Instruction.Visuals.DrawLine.OptionalFields.COLOR_FIELD,
            name = { it.name },
            instruction = instruction,
            instructionField = instruction.color,
            onValueChanged = onEditInstruction,
            onDisable = onEditInstruction
        )

        // Add button if not all optional fields are enabled
        if (instruction.base.enabledOptionalFields.size < Instruction.Visuals.DrawLine.OptionalFields.entries.size) {
            EnableInstructionFieldsButton(onClick = onAddInstructionField)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawLineBlockPreview() {
    DrawLineInstructionBlock(
        instruction = InstructionRuntime.Visuals.DrawLine(Instruction.Visuals.DrawLine()),
        onEditInstruction = {},
        onAddInstructionField = {}
    )
}
