package farid.guliyev.mblockly.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import farid.guliyev.mblockly.domain.model.Instruction

data class DraggableUiBlock <T: Instruction>(
    val instruction: T
) {
    private var _x = mutableFloatStateOf(0F)
    private var _y = mutableFloatStateOf(0F)

    fun updatePositionBy(offset: Offset) {
        val newX = (_x.floatValue + offset.x).coerceAtLeast(0F)
        val newY = (_y.floatValue + offset.y).coerceAtLeast(0F)

        _x.floatValue = newX
        _y.floatValue = newY
    }

    val x get() =  _x.floatValue
    val y get() =  _y.floatValue
}
