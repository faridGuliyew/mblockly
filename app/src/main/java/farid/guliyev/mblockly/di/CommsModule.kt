package farid.guliyev.mblockly.di

import farid.guliyev.mblockly.core.exception_handling.asAlert
import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.ui.components.dialog.Confirmation
import kotlinx.coroutines.channels.Channel

object CommsModule {

    val alertChannel = Channel<Alert>()
    val confirmationChannel = Channel<Confirmation<*>>()
    val confirmationFeedbackChannel = Channel<Any?>()
}

suspend fun Channel<Alert>.sendException(e: Exception) {
    send(e.asAlert())
}

fun Channel<Alert>.trySendException(e: Exception) {
    trySend(e.asAlert())
}