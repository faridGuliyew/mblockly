package farid.guliyev.mblockly.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFD32F2F) // red when error
            isFocused -> Color(0xFF1976D2) // stronger blue on focus
            else -> Color(0xFF90CAF9) // soft blue default
        },
        label = ""
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFFFEBEE) // light red on error
            isFocused -> Color(0xFFE3F2FD) // light blue on focus
            else -> Color.White
        },
        label = ""
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 14.sp,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color(0xFF1976D2)),
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .width(IntrinsicSize.Min)
    )
}



@Preview
@Composable
private fun CustomTextFieldPrev() {
    CustomTextField(
        value = "Hi!",
        onValueChange = {}
    )
}