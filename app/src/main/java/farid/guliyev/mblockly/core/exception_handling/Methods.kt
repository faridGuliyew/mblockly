package farid.guliyev.mblockly.core.exception_handling

import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.domain.model.AlertType
import farid.guliyev.mblockly.domain.model.ExceptionType
import farid.guliyev.mblockly.ui.extensions.label
import farid.guliyev.mblockly.ui.extensions.toAlertType

fun failGracefully(message: String, type: ExceptionType = ExceptionType.FATAL) : Nothing {
    throw when (type) {
        ExceptionType.FATAL -> AppException.FatalException(message)
        ExceptionType.WARNING -> AppException.WarningException(message)
        ExceptionType.INFO -> AppException.InfoException(message)
    }
}

fun Exception.asAlert() : Alert {
    val alertType = (this as? AppException)?.type?.toAlertType() ?: AlertType.ERROR

    return Alert(
        title = alertType.label,
        description = message ?: "No description available",
        type = alertType
    )
}