package farid.guliyev.mblockly.ui.model

import androidx.compose.runtime.MutableFloatState
import farid.guliyev.mblockly.domain.model.Shape

data class UiShape(
    val width: MutableFloatState,
    val height: MutableFloatState,
    val cornerRadius: MutableFloatState,
    val x: MutableFloatState,
    val y: MutableFloatState,
    val scale: MutableFloatState,
    val color: Long
)