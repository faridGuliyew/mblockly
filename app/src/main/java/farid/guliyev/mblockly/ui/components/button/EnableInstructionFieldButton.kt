package farid.guliyev.mblockly.ui.components.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.ui.theme.AccentEmerald
import farid.guliyev.mblockly.ui.theme.NeutralGray50
import farid.guliyev.mblockly.ui.theme.NeutralGray700

@Composable
fun EnableInstructionFieldButton(
    label: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { 
            Text(
                text = label,
                color = NeutralGray700,
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            ) 
        },
        leadingIcon = {
            Icon(
                Icons.Default.Add, 
                contentDescription = null, 
                modifier = Modifier.size(18.dp),
                tint = AccentEmerald
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = NeutralGray50,
            labelColor = NeutralGray700
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = AccentEmerald.copy(alpha = 0.3f)
        )
    )
}

@Preview
@Composable
private fun EnableInstructionFieldButtonPrev() {
    EnableInstructionFieldButton(label = "Hi", onClick = {})
}