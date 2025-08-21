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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgrounded
import farid.guliyev.mblockly.ui.theme.AccentEmeraldDark
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.NeutralGray400
import farid.guliyev.mblockly.ui.theme.NeutralGray900

@Composable
fun OutputTopBar(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit
) {
    val shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    Column (
        modifier = modifier
            .clip(shape)
            .background(color = BackgroundSecondary)
            .border(width = 1.dp, color = NeutralGray400, shape = shape)
    ) {
        Box (contentAlignment = Alignment.Center) {
            HorizontalDivider(thickness = 10.dp, color = NeutralGray900)
            Box(modifier = Modifier.width(50.dp).height(2.dp).background(color = BackgroundPrimary))
        }
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
    }
}

@Preview
@Composable
private fun OutputTopBarPrev() {
    OutputTopBar(onFinish = {})
}