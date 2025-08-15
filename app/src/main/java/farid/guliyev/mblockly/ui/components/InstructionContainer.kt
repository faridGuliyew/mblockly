package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.domain.model.InstructionType

@Composable
fun InstructionContainer(
    type: InstructionType,
    index: Int,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFBBDEFB), RoundedCornerShape(16.dp)) // soft blue card
            .border(2.dp, Color(0xFF1976D2), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            // Index badge
            InstructionIndex(index = index)
            Spacer(modifier = Modifier.width(4.dp))
            // Label
            Text(
                modifier = Modifier.weight(1F),
                text = type.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
            // Move up
            AppIconButton(onClick = onMoveUp, icon = Icons.Default.KeyboardArrowUp, color = Color(0xFF1976D2))
            // Move down
            AppIconButton(onClick = onMoveDown, icon = Icons.Default.KeyboardArrowDown, color = Color(0xFF1976D2))
            // Remove button
            AppIconButton(onClick = onRemove, icon = Icons.Default.Close, color = Color(0xFFD32F2F))
        }
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}