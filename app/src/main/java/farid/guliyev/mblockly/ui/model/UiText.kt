package farid.guliyev.mblockly.ui.model
import androidx.compose.runtime.State
data class UiText(
    val text: State<String>,
    val x: State<Float>,
    val y: State<Float>,
    val color: Long,
    val fontSize: State<Float>
)