package farid.guliyev.mblockly.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.di.CommsModule
import farid.guliyev.mblockly.di.sendException
import farid.guliyev.mblockly.di.trySendException
import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.domain.model.AlertType
import farid.guliyev.mblockly.domain.model.ExceptionType
import farid.guliyev.mblockly.ui.components.dialog.Confirmation
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val sheetState = MutableStateFlow(SheetType.HIDDEN)

    protected fun showSheet(type: SheetType) {
        sheetState.update { type }
    }

    fun showSuccessAlert(message: String) {
        CommsModule.alertChannel.trySend(
            Alert(
                title = "YEE-HAW!",
                description = message,
                type = AlertType.SUCCESS
            )
        )
    }

    suspend fun showConfirmation(description: String) {
        CommsModule.confirmationChannel.send(Confirmation(description = description))
        val isConfirmed = CommsModule.confirmationFeedbackChannel.receive()

        if (!isConfirmed) failGracefully("Action is cancelled!", ExceptionType.INFO)
    }

    fun hideSheet() {
        sheetState.update { SheetType.HIDDEN }
    }

    /** WARNING: This method is not suspending/blocking. Runs in a background thread. Use suspend variant for synchronous operations */
    fun runSafelyInBg(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        operation: suspend () -> Unit,
    ) {
        viewModelScope.launch(dispatcher) {
            runSafelySuspend(operation)
        }
    }

    fun runSafelyBlocking(
        operation: () -> Unit,
    ) {
        try {
            operation()
        } catch (e: Exception) {
            CommsModule.alertChannel.trySendException(e)
        }
    }

    suspend fun <T> runSafelySuspend(
        operation: suspend () -> T,
    ): T? {
        return try {
            operation()
        } catch (e: Exception) {
            CommsModule.alertChannel.sendException(e)
            null
        }
    }
}