package farid.guliyev.mblockly.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.ui.theme.PrimaryBlue
import farid.guliyev.mblockly.ui.theme.NeutralGray50

@Composable
fun EnableInstructionFieldsButton(
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp)),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp, 
            Brush.horizontalGradient(
                colors = listOf(
                    PrimaryBlue.copy(alpha = 0.6f),
                    PrimaryBlue.copy(alpha = 0.3f)
                )
            )
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = NeutralGray50,
            contentColor = PrimaryBlue
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add fields",
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Add fields", 
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}