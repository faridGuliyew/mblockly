package farid.guliyev.mblockly.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.ui.theme.AccentEmerald
import farid.guliyev.mblockly.ui.theme.AccentEmeraldDark
import farid.guliyev.mblockly.ui.theme.NeutralGray50

@Composable
fun AddInstructionButton(
    modifier: Modifier = Modifier,
    text: String = "Add block",
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
//            .border(shape = RoundedCornerShape(24.dp), width = 1.dp, color = AccentEmeraldDark)
        ,
        border = BorderStroke(
            width = 1.dp,
            color = AccentEmeraldDark
        ),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = NeutralGray50
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
        )
    }
}