package farid.guliyev.mblockly.ui.model

import androidx.compose.runtime.State

data class UiImage(
    val width: State<Float>,
    val height: State<Float>,
    val x: State<Float>,
    val y: State<Float>,
    val absolutePath: String
)

