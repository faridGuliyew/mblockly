package farid.guliyev.mblockly.ui.model

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import farid.guliyev.mblockly.domain.model.Shape
import kotlinx.coroutines.flow.Flow

data class UiShape(
    val width: State<Float>,
    val height: State<Float>,
    val cornerRadius: State<Float>,
    val x: State<Float>,
    val y: State<Float>,
    val scale: State<Float>,
    val rotation: State<Float>,
    val color: Long
)