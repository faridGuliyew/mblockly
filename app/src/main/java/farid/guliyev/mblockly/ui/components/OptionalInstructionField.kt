package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.InstructionField
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.theme.ErrorRed

@Composable
fun <T, P: InstructionRuntime, O> OptionalInstructionField(
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
            instructionField = instructionField,
            onValueChanged = onValueChanged,
            trailingContent = {
                AppIconButton(
                    icon = Icons.Default.Close,
                    color = ErrorRed,
                    onClick = {
                        onDisable(InstructionRuntime.fromBase(instruction.base.changeOptionalFieldByName(name(optionalField), isEnabled = false)) as P)
                    }
                )
            }
        )
    }
}
