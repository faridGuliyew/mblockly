package farid.guliyev.mblockly.core.exception_handling

import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.domain.model.AlertType
import farid.guliyev.mblockly.domain.model.ExceptionType
import farid.guliyev.mblockly.ui.extensions.label
import farid.guliyev.mblockly.ui.extensions.toAlertType

sealed class AppException: Exception() {
    data class FatalException(override val message: String) : AppException()
    data class WarningException(override val message: String) : AppException()
    data class InfoException(override val message: String) : AppException()
}

val AppException.type get() =  when(this) {
    is AppException.FatalException -> ExceptionType.FATAL
    is AppException.InfoException -> ExceptionType.INFO
    is AppException.WarningException -> ExceptionType.WARNING
}