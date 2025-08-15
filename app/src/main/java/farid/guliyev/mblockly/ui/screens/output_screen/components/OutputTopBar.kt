package farid.guliyev.mblockly.ui.screens.output_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OutputTopBar(
    onFinish: () -> Unit
) {
    Column (
        modifier = Modifier
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = "OUTPUT"
            )

            IconButton(
                onClick = onFinish
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "add"
                )
            }
        }

        HorizontalDivider(thickness = 3.dp)
    }
}

@Preview
@Composable
private fun OutputTopBarPrev() {
    OutputTopBar(onFinish = {})
}