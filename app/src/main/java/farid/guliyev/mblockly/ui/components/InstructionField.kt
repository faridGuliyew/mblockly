package farid.guliyev.mblockly.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.domain.model.instruction.InstructionField
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.ErrorRed
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.PrimaryBlue

val FloatValidator: (String) -> Boolean = { it.toFloatOrNull() != null }
val FloatConverter: (String) -> Float = { it.toFloatOrNull() ?: 0.0F }
val ColorValidator: (String) -> Boolean = { it.toLongOrNull(16) != null }
val IntValidator: (String) -> Boolean = { it.toIntOrNull() != null }
val StringValidator: (String) -> Boolean = { it.isNotBlank() }
val StringConverter: (String) -> String = { it }

@Composable
fun <T, P> InstructionField(
    instructionField: InstructionField<T, P>,
    onValueChanged: (P) -> Unit,
    trailingContent: @Composable () -> Unit = {}
) {
    var isInputValid by remember { mutableStateOf(true) }
    var input by remember(instructionField.value) { mutableStateOf(instructionField.toString()) }

    val backgroundColor by animateColorAsState(
        if (isInputValid) BackgroundPrimary else ErrorRed.copy(alpha = 0.05f),
        label = ""
    )
    val borderColor by animateColorAsState(
        if (isInputValid) PrimaryBlue else ErrorRed,
        label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.9f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = instructionField.label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isInputValid) NeutralGray700 else ErrorRed
        )

        CustomTextField(
            value = input,
            onValueChange = { newInput ->
                input = newInput
                isInputValid = instructionField.validator(newInput)

                if (!isInputValid) return@CustomTextField
                onValueChanged(instructionField.onEdit(newInput))
            },
            isError = !isInputValid
        )

        trailingContent()
    }
}
