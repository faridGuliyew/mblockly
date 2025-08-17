package farid.guliyev.mblockly.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import farid.guliyev.mblockly.domain.model.AlertType
import farid.guliyev.mblockly.domain.model.ExceptionType
import farid.guliyev.mblockly.ui.theme.ErrorRed
import farid.guliyev.mblockly.ui.theme.SuccessGreen
import farid.guliyev.mblockly.ui.theme.WarningAmber
import farid.guliyev.mblockly.ui.theme.InfoBlue

val AlertType.containerColor
    @Composable
    get() = when(this) {
        AlertType.ERROR -> ErrorRed.copy(alpha = 0.15f)
        AlertType.SUCCESS -> SuccessGreen.copy(alpha = 0.15f)
        AlertType.WARNING -> WarningAmber.copy(alpha = 0.15f)
        AlertType.INFO -> InfoBlue.copy(alpha = 0.15f)
    }

val AlertType.contentColor
    @Composable
    get() = when(this) {
        AlertType.ERROR -> ErrorRed
        AlertType.SUCCESS -> SuccessGreen
        AlertType.WARNING -> WarningAmber
        AlertType.INFO -> InfoBlue
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