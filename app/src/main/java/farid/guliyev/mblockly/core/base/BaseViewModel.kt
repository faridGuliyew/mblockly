package farid.guliyev.mblockly.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import farid.guliyev.mblockly.di.CommsModule
import farid.guliyev.mblockly.di.sendException
import farid.guliyev.mblockly.di.trySendException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

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

    suspend fun runSafelySuspend(
        operation: suspend () -> Unit,
    ) {
        try {
            operation()
        } catch (e: Exception) {
            CommsModule.alertChannel.sendException(e)
        }
    }
}