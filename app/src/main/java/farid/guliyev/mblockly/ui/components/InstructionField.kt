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
    CustomTextFieldContainer {
        Text(
            text = instructionField.label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeutralGray700
        )

        CustomTextFieldWithValidator(
            originalValue = instructionField.value,
            onValueChange = { newInput ->
                onValueChanged(instructionField.onEdit(newInput))
            }, validator = instructionField.validator
        )

        trailingContent()
    }
}
