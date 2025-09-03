package farid.guliyev.mblockly.ui.screens.builder_screen.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.ui.components.InstructionField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrawImageInstructionBlock(
    instruction: InstructionRuntime.Visuals.DrawImage,
    onEditInstruction: (InstructionRuntime.Visuals.DrawImage) -> Unit,
    onAddInstructionField: () -> Unit
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InstructionField(
            instructionField = instruction.name,
            onValueChanged = onEditInstruction
        )

        InstructionField(
            instructionField = instruction.imageName,
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
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawImageBlockComponentPrev() {
    DrawImageInstructionBlock(
        instruction = InstructionRuntime.Visuals.DrawImage(Instruction.Visuals.DrawImage()),
        onEditInstruction = {},
        onAddInstructionField = {}
    )
}

