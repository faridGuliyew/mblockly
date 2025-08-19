package farid.guliyev.mblockly.ui.screens.builder_screen.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.ui.model.DraggableUiBlock

@Composable
fun <T: Instruction> DraggableBlockComponent(
    block: DraggableUiBlock<T>,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .graphicsLayer {
                translationX = block.x
                translationY = block.y
            }.pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    block.updatePositionBy(dragAmount)
                }
            }
    ) {
        content()
    }
}

@Preview
@Composable
private fun BlockComponentPrev() {
    DraggableBlockComponent(block = DraggableUiBlock(instruction = Instruction.Visuals.DrawShape()), content = {}
    )
}