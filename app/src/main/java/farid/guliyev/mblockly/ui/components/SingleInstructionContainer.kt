package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.instruction.SingleInstructionType

@Composable
fun SingleInstructionContainer(
    type: SingleInstructionType,
    isMinimized: Boolean,
    index: Int,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
    onToggleMinimize: () -> Unit,
    onDuplicate: () -> Unit,
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
        onDuplicate = onDuplicate,
        content = {
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    )
}

@Preview
@Composable
private fun SingleInstructionContainerPrev() {
    SingleInstructionContainer(
        type = SingleInstructionType.ANIMATE_FLOAT,
        isMinimized = false,
        index = 1,
        onRemove = {},
        onMoveUp = {},
        onMoveDown = {},
        onToggleMinimize = {},
        content = {},
        onMoveIn = {},
        onMoveOut = {},
        onDuplicate = {}

    )
}