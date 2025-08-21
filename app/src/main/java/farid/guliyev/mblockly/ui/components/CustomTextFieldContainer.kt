package farid.guliyev.mblockly.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.ErrorRed
import farid.guliyev.mblockly.ui.theme.PrimaryBlue

@Composable
fun CustomTextFieldContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val backgroundColor by animateColorAsState(
        BackgroundPrimary,
        label = ""
    )
    val borderColor by animateColorAsState(
        PrimaryBlue,
        label = ""
    )

    Row(
        modifier = modifier
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        content()
    }
}