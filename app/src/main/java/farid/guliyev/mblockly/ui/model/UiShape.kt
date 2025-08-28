package farid.guliyev.mblockly.ui.model

import androidx.compose.runtime.State

data class UiLine(
    val startX : State<Float>,
    val startY: State<Float>,
    val endX: State<Float>,
    val endY: State<Float>,
    val thickness: State<Float>,
    val color: Long
)