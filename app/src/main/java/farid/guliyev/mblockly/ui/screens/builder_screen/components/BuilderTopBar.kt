package farid.guliyev.mblockly.ui.screens.builder_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.domain.model.InstructionType
import farid.guliyev.mblockly.ui.components.button.EnableInstructionFieldButton
import farid.guliyev.mblockly.ui.theme.BackgroundSecondary
import farid.guliyev.mblockly.ui.theme.NeutralGray100
import farid.guliyev.mblockly.ui.theme.NeutralGray200
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.SuccessGreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Share
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgrounded
import farid.guliyev.mblockly.ui.theme.InfoBlue

enum class TopBarMode {
    ADD_INSTRUCTION, ENABLE_FIELD, HIDDEN
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuilderTopBar(
    mode: TopBarMode = TopBarMode.ADD_INSTRUCTION,
    supportedInstructions: List<InstructionType> = InstructionType.entries,
    onAddInstruction: (InstructionType) -> Unit,
    enableFieldList: List<String> = emptyList(),
    onEnableField: (String) -> Unit,
    onExecute: () -> Unit,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onHide: () -> Unit,
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(color = BackgroundSecondary)
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButton(
                icon = Icons.Default.KeyboardArrowLeft,
                color = NeutralGray700,
                onClick = onShare
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                modifier = Modifier.weight(1F),
                text = "Builder panel",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralGray700
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (mode != TopBarMode.HIDDEN) {
                    AppIconButtonBackgrounded(
                        icon = Icons.Default.KeyboardArrowUp,
                        color = NeutralGray700,
                        onClick = onHide
                    )
                }

                AppIconButtonBackgrounded(
                    icon = Icons.Default.Share,
                    color = InfoBlue,
                    onClick = onShare
                )

                AppIconButtonBackgrounded(
                    icon = Icons.Default.PlayArrow,
                    color = SuccessGreen,
                    onClick = onExecute
                )
            }
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = NeutralGray200.copy(alpha = 0.5f)
        )

        AnimatedContent(mode) {
            when (it) {
                TopBarMode.ADD_INSTRUCTION -> {
                    AddBlockPanel(
                        blocks = supportedInstructions,
                        onClick = {
                            onHide()
                            onAddInstruction(it)
                        }
                    )
                }

                TopBarMode.ENABLE_FIELD -> {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        enableFieldList.forEach {
                            EnableInstructionFieldButton(
                                label = it,
                                onClick = {
                                    onHide()
                                    onEnableField(it)
                                }
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
            .padding(16.dp)
    ) {
        blocks.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = {
                        onClick(type)
                    })
                    .background(NeutralGray100)
                    .padding(8.dp)
            ) {
                Text(
                    text = type.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = NeutralGray700
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = NeutralGray200.copy(alpha = 0.3f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPrev() {
    BuilderTopBar(
        onAddInstruction = {}, onExecute = {}, onHide = {},
        mode = TopBarMode.ADD_INSTRUCTION,
        supportedInstructions = listOf(),
        enableFieldList = listOf(),
        onEnableField = {},
        onShare = {},
        onBack = {}
    )
}