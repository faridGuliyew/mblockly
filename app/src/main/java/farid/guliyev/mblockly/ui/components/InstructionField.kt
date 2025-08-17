package farid.guliyev.mblockly.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.InstructionField

val FloatValidator: (String) -> Boolean = { it.toFloatOrNull() != null }
val FloatConverter: (String) -> Float = { it.toFloatOrNull() ?: 0.0F }
val ColorValidator: (String) -> Boolean = { it.toLongOrNull(16) != null }
val IntValidator: (String) -> Boolean = { it.toIntOrNull() != null }
val StringValidator: (String) -> Boolean = { it.isNotBlank() }
val StringConverter: (String) -> String = { it }

@Composable
fun <T, P> InstructionField(
    instruction: P,
    instructionField: InstructionField<T, P>,
    onValueChanged: (P) -> Unit,
) {
    var isInputValid by remember { mutableStateOf(true) }
    var input by remember(instructionField.value) { mutableStateOf(instructionField.toString()) }

    val backgroundColor by animateColorAsState(
        if (isInputValid) Color(0xFFE3F2FD) else Color(0xFFFFEBEE),
        label = ""
    )
    val borderColor by animateColorAsState(
        if (isInputValid) Color(0xFF2196F3) else Color(0xFFD32F2F),
        label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = instructionField.label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = borderColor
        )
        Spacer(Modifier.width(4.dp))
        CustomTextField(
            value = input,
            onValueChange = { newInput ->
                input = newInput
                isInputValid = instructionField.validator(newInput)

                if (!isInputValid) return@CustomTextField
                onValueChanged(instructionField.onEdit(instruction, newInput))
            },
            isError = !isInputValid
        )
    }
}
