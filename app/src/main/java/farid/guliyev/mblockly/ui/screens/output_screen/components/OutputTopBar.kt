package farid.guliyev.mblockly.ui.screens.output_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.ui.theme.BackgroundSecondary
import farid.guliyev.mblockly.ui.theme.NeutralGray200
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.ErrorRed
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgrounded

@Composable
fun OutputTopBar(
    onFinish: () -> Unit
) {
    Column (
        modifier = Modifier
            .statusBarsPadding()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(color = BackgroundSecondary)
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = "OUTPUT",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralGray700
            )

            AppIconButtonBackgrounded(
                icon = Icons.Default.Close,
                color = ErrorRed,
                onClick = onFinish
            )
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = NeutralGray200.copy(alpha = 0.5f)
        )
    }
}

@Preview
@Composable
private fun OutputTopBarPrev() {
    OutputTopBar(onFinish = {})
}