package farid.guliyev.mblockly.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import farid.guliyev.mblockly.domain.model.instruction.InstructionType

@Composable
fun SingleInstructionContainer(
    type: InstructionType,
    isMinimized: Boolean,
    index: Int,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
    onToggleMinimize: () -> Unit,
    content: @Composable () -> Unit
) {
    InstructionContainer(
        label = type.label,
        isMinimized = isMinimized,
        index = index,
        onRemove = onRemove,
        onMoveUp = onMoveUp,
        onMoveOut = onMoveOut,
        onMoveIn = onMoveIn,
        onMoveDown = onMoveDown,
        onToggleMinimize = onToggleMinimize,
        content = content
    )
}

@Preview
@Composable
private fun SingleInstructionContainerPrev() {
    SingleInstructionContainer(
        type = InstructionType.ANIMATE_FLOAT,
        isMinimized = false,
        index = 1,
        onRemove = {},
        onMoveUp = {},
        onMoveDown = {},
        onToggleMinimize = {},
        content = {},
        onMoveIn = {},
        onMoveOut = {}

    )
}