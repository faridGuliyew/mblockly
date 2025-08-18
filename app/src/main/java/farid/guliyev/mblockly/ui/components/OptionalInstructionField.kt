package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.InstructionField
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.theme.ErrorRed

@Composable
fun <T, P: Instruction, O> OptionalInstructionField(
    enabledOptionalFields: Set<O>,
    optionalField: O,
    name: (O) -> String,
    instruction: P,
    instructionField: InstructionField<T, P>,
    onValueChanged: (P) -> Unit,
    onDisable: (P) -> Unit
) {
    if (!enabledOptionalFields.contains(optionalField)) return

    Row {
        InstructionField(
            instruction = instruction,
            instructionField = instructionField,
            onValueChanged = onValueChanged,
            trailingContent = {
                AppIconButton(
                    icon = Icons.Default.Close,
                    color = ErrorRed,
                    onClick = {
                        onDisable(instruction.changeOptionalFieldByName(name(optionalField), isEnabled = false) as P)
                    }
                )
            }
        )
    }
}
