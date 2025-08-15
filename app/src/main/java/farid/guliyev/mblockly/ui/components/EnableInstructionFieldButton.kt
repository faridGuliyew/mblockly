package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EnableInstructionFieldButton(
    label: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    )
}

@Preview
@Composable
private fun EnableInstructionFieldButtonPrev() {
    EnableInstructionFieldButton(label = "Hi", onClick = {})
}