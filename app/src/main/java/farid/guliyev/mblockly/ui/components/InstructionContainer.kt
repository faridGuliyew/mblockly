package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.R
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.theme.BackgroundSecondary
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.PrimaryBlue
import farid.guliyev.mblockly.ui.theme.SuccessGreen
import farid.guliyev.mblockly.ui.theme.ErrorRed

@Composable
fun InstructionContainer(
    modifier: Modifier = Modifier,
    label: String,
    index: Int,
    isMinimized: Boolean,
    isActive: Boolean,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveIn: () -> Unit,
    onMoveOut: () -> Unit,
    onToggleMinimize: () -> Unit,
    onDuplicate: (() -> Unit)? = null,
    onEditSeparately: (() -> Unit)? = null,
    onToggleActive: () -> Unit,
    borderColor: Color = PrimaryBlue,
    backgroundColor: Color = BackgroundSecondary,
    innerPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.8f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(innerPadding)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Row (
                modifier = Modifier.weight(1F)
                    .clickable(onClick = onToggleMinimize),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Index badge
                InstructionIndex(index = index)
                // Label
                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralGray700,
                    style = TextStyle(
                        textDecoration = if (!isActive) TextDecoration.LineThrough else null
                    )
                )
            }
            Row (horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Move up
                AppIconButton(
                    onClick = onMoveUp,
                    onLongClick = onMoveOut,
                    icon = Icons.Default.KeyboardArrowUp,
                    color = PrimaryBlue
                )
                // Move down
                AppIconButton(
                    onClick = onMoveDown,
                    onLongClick = onMoveIn,
                    icon = Icons.Default.KeyboardArrowDown,
                    color = PrimaryBlue
                )
                onDuplicate?.let {
                    // Minimize button
                    AppIconButton(onClick = onDuplicate, icon = ImageVector.vectorResource(R.drawable.ic_copy), color = NeutralGray700)
                }
                onEditSeparately?.let {
                    // Edit separately button
                    AppIconButton(onClick = onEditSeparately, icon = Icons.Default.ExitToApp, color = SuccessGreen)
                }
                // Toggle isActive button
                AppIconButton(onClick = onToggleActive, icon = Icons.Default.Build, color = NeutralGray700)

                // Remove button
                AppIconButton(onClick = onRemove, icon = Icons.Default.Close, color = ErrorRed)
            }
        }

        if (isMinimized) return@Column

        content()
    }
}

@Preview
@Composable
private fun InstructionContainerPrev() {
    InstructionContainer(
        label = "Thread: Main",
        index = 1,
        onRemove = {},
        onMoveUp = {},
        onMoveDown = {},
        onMoveOut = {},
        onMoveIn = {},
        content = {},
        innerPadding = PaddingValues(vertical = 4.dp, horizontal = 2.dp),
        isMinimized = false,
        isActive = false,
        onToggleMinimize = {},
        onDuplicate = {},
        onToggleActive = {}

    )
}