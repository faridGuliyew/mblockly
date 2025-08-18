package farid.guliyev.mblockly.ui.components.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.NeutralGray800

enum class SheetType {
    HIDDEN, SHARE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    modifier: Modifier = Modifier,
    type: SheetType,
    isFullyExpanded: Boolean = true,
    showDragHandle: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.(SheetType) -> Unit)
) {

    if (type != SheetType.HIDDEN){
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = isFullyExpanded,
        )

        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            containerColor = BackgroundPrimary,
            dragHandle = if (showDragHandle) { { DragTopLine() } } else null,
            shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp),
            onDismissRequest = onDismissRequest,
            content = {
                content(type)
            }
        )
    }
}

@Composable
private fun DragTopLine(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Spacer(
            modifier
                .padding(top = 16.dp)
                .width(36.dp)
                .height(4.dp)
                .background(NeutralGray800, CircleShape)
        )
    }
}