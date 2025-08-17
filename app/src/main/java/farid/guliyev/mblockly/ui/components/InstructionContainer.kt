package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InstructionContainer(
    modifier: Modifier = Modifier,
    label: String,
    index: Int,
    isMinimized: Boolean,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveIn: () -> Unit,
    onMoveOut: () -> Unit,
    onToggleMinimize: (() -> Unit)? = null,
    onEditSeparately: (() -> Unit)? = null,
    borderColor: Color = Color(0xFF1976D2),
    backgroundColor: Color = Color(0xFFBBDEFB),
    innerPadding: PaddingValues = PaddingValues(12.dp),
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(16.dp)) // soft blue card
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(innerPadding)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            // Index badge
            InstructionIndex(index = index)
            Spacer(modifier = Modifier.width(4.dp))
            // Label
            Text(
                modifier = Modifier.weight(1F),
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
            // Move up
            AppIconButton(
                onClick = onMoveUp,
                onLongClick = onMoveOut,
                icon = Icons.Default.KeyboardArrowUp,
                color = Color(0xFF1976D2)
            )
            // Move down
            AppIconButton(
                onClick = onMoveDown,
                onLongClick = onMoveIn,
                icon = Icons.Default.KeyboardArrowDown,
                color = Color(0xFF1976D2)
            )
            // Remove button
            AppIconButton(onClick = onRemove, icon = Icons.Default.Close, color = Color(0xFFD32F2F))
            onToggleMinimize?.let {
                // Minimize button
                AppIconButton(onClick = onToggleMinimize, icon = Icons.Default.MoreVert, color = Color.Black)
            }
            onEditSeparately?.let {
                // Edit separately button
                AppIconButton(onClick = onEditSeparately, icon = Icons.Default.ExitToApp, color = Color.Black)
            }
        }

        if (isMinimized) return@Column

        Spacer(modifier = Modifier.height(8.dp))
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

    )
}