package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.domain.model.instruction.type
import farid.guliyev.mblockly.ui.components.SingleInstructionContainer
import farid.guliyev.mblockly.ui.components.InstructionField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimateFloatInstructionBlock(
    instruction: InstructionRuntime.Animations.AnimateFloat,
    onEditInstruction: (InstructionRuntime.Animations.AnimateFloat) -> Unit,
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
            instructionField = instruction.valueField,
            onValueChanged = onEditInstruction
        )

        InstructionField(
            instructionField = instruction.durationField,
            onValueChanged = onEditInstruction
        )
    }
}