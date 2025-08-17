package farid.guliyev.mblockly.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import farid.guliyev.mblockly.domain.model.AlertType
import farid.guliyev.mblockly.domain.model.ExceptionType

val AlertType.containerColor
    @Composable
    get() = when(this) {
        AlertType.ERROR -> Color(0xFFFFCDD2)
        AlertType.SUCCESS -> Color(0xFFC8E6C9)
        AlertType.WARNING -> Color(0xFFFFF9C4)
        AlertType.INFO -> Color(0xFFBBDEFB)
    }

val AlertType.contentColor
    @Composable
    get() = when(this) {
        AlertType.ERROR -> Color(0xFFD32F2F)
        AlertType.SUCCESS -> Color(0xFF388E3C)
        AlertType.WARNING -> Color(0xFFFBC02D)
        AlertType.INFO -> Color(0xFF1976D2)
    }

val AlertType.label
    get() = when(this) {
        AlertType.ERROR -> "OOPS!"
        AlertType.SUCCESS -> "YAY!"
        AlertType.WARNING -> "WATCH OUT"
        AlertType.INFO -> "Info"
    }

fun ExceptionType.toAlertType() : AlertType {
    return when(this) {
        ExceptionType.FATAL -> AlertType.ERROR
        ExceptionType.WARNING -> AlertType.WARNING
        ExceptionType.INFO -> AlertType.INFO
    }
}