package farid.guliyev.mblockly.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.Shape

class MovableUiShape(
    val shape: Shape,
    x: Float = 0.0F,
    y: Float = 0.0F
) {
    var x by mutableFloatStateOf(x)
    var y by mutableFloatStateOf(y)
}

fun Instruction.Visuals.DrawShape.toMovableShape() = MovableUiShape(shape = Shape(
    color = color.value,
    width = width.value,
    height = height.value,
    cornerRadius = cornerRadius.value
), x = x.value, y = y.value)