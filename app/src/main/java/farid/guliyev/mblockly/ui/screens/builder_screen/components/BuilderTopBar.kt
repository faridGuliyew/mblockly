package farid.guliyev.mblockly.ui.screens.builder_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.model.InstructionType
import farid.guliyev.mblockly.ui.components.EnableInstructionFieldButton

enum class TopBarMode {
    INSTRUCTION, ENABLE_FIELD, HIDDEN
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuilderTopBar(
    mode: TopBarMode = TopBarMode.INSTRUCTION,
    supportedInstructions: List<InstructionType> = InstructionType.entries,
    onAddInstruction: (InstructionType) -> Unit,
    enableFieldList: List<String> = emptyList(),
    onEnableField: (String) -> Unit,
    onExecute: () -> Unit,
    onToggleExpand: () -> Unit
) {
    Column (
        modifier = Modifier
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = "Builder panel"
            )

            Row {
                IconButton(
                    onClick = onToggleExpand
                ) {
                    Crossfade(
                        targetState = mode != TopBarMode.HIDDEN
                    ) { isExpanded ->
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.Add,
                            contentDescription = "add"
                        )
                    }
                }


                IconButton(
                    onClick = onExecute
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "run"
                    )
                }
            }
        }

        HorizontalDivider(thickness = 3.dp)

        AnimatedContent(mode) {
            when (it) {
                TopBarMode.INSTRUCTION -> {
                    AddBlockPanel(
                        blocks = supportedInstructions,
                        onClick = {
                            onToggleExpand()
                            onAddInstruction(it)
                        }
                    )
                }
                TopBarMode.ENABLE_FIELD -> {
                    FlowRow (horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        enableFieldList.forEach {
                            EnableInstructionFieldButton(
                                label = it,
                                onClick = { onEnableField(it) }
                            )
                        }
                    }
                }
                TopBarMode.HIDDEN -> Unit
            }
        }
    }
}

@Composable
fun AddBlockPanel(blocks: List<InstructionType>, onClick: (InstructionType) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        blocks.forEach { type ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        onClick(type)
                    })
                    .padding(16.dp)
            ) {
                Text(type.name)
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPrev() {
    BuilderTopBar(onAddInstruction = {}, onExecute = {}, onToggleExpand = {},
        mode = TopBarMode.INSTRUCTION,
        supportedInstructions = listOf(),
        enableFieldList = listOf(),
        onEnableField = {}, )
}