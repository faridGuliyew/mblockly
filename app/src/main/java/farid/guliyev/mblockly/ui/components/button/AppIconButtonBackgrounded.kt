package farid.guliyev.mblockly.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AppIconButtonBackgrounded(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    isHighlighted: Boolean = false,
    onClick: () -> Unit,
    trailingContent: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color.copy(alpha = 0.1f))
            .then(if (isHighlighted) Modifier.border(
                width = 1.dp,
                color = color,
                shape = shape
            ) else Modifier)
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
            trailingContent()
        }
    }
}

@Composable
fun AppIconButtonBackgroundedWithText(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    color: Color,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    isHighlighted: Boolean = false,
    onClick: () -> Unit
) {
    AppIconButtonBackgrounded(
        modifier = modifier,
        icon = icon,
        color = color,
        onClick = onClick,
        shape = shape,
        isHighlighted = isHighlighted,
        trailingContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = text,
                color = color
            )
        }
    )
}