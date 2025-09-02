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
fun PlaySoundInstructionBlock(
    instruction: InstructionRuntime.Media.PlaySound,
    onEditInstruction: (InstructionRuntime.Media.PlaySound) -> Unit,
    onAddInstructionField: () -> Unit
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InstructionField(
            instructionField = instruction.fileName,
            onValueChanged = onEditInstruction
        )

        InstructionField(
            instructionField = instruction.repeatCount,
            onValueChanged = onEditInstruction
        )
    }
}