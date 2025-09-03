package farid.guliyev.mblockly.ui.screens.builder_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
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
import farid.guliyev.mblockly.domain.model.instruction.SingleInstructionType
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgrounded
import farid.guliyev.mblockly.ui.components.button.EnableInstructionFieldButton
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionGroupMetaData
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionGroupType
import farid.guliyev.mblockly.ui.screens.builder_screen.init
import farid.guliyev.mblockly.ui.theme.InfoBlue
import farid.guliyev.mblockly.ui.theme.NeutralGray100
import farid.guliyev.mblockly.ui.theme.NeutralGray200
import farid.guliyev.mblockly.ui.theme.NeutralGray500
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.SuccessGreen
import farid.guliyev.mblockly.ui.theme.WarningAmber

enum class TopBarMode {
    ADD_INSTRUCTION,
    ADD_GROUP,
    ENABLE_FIELD,
    HIDDEN
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuilderTopBar(
    mode: TopBarMode = TopBarMode.ADD_INSTRUCTION,
    projectName: String,
    supportedInstructions: List<SingleInstructionType> = SingleInstructionType.entries,
    onAddSingleInstruction: (SingleInstructionType) -> Unit,
    onAddInstructionGroup: (InstructionGroupMetaData) -> Unit,
    enableFieldList: List<String> = emptyList(),
    onEnableField: (String) -> Unit,
    onExecute: () -> Unit,
    onHide: () -> Unit,
    onShare: () -> Unit,
    onOpenAssets: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButton(
                icon = Icons.Default.KeyboardArrowLeft,
                color = NeutralGray700,
                onClick = onBack
            )
            Spacer(modifier = Modifier.width(10.dp))

            Column (modifier = Modifier.weight(1F)) {
                Text(
                    text = "Builder panel",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralGray700
                )
                Text(
                    text = projectName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = NeutralGray500
                )
            }

            Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (mode != TopBarMode.HIDDEN) {
                    AppIconButtonBackgrounded(
                        icon = Icons.Default.KeyboardArrowUp,
                        color = NeutralGray700,
                        onClick = onHide
                    )
                }

                AppIconButtonBackgrounded(
                    icon = Icons.Default.Menu,
                    color = WarningAmber,
                    onClick = onOpenAssets
                )

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
                            onAddSingleInstruction(it)
                        }
                    )
                }
                TopBarMode.ADD_GROUP -> {
                    AddGroupPanel(
                        onClick = {
                            onHide()
                            onAddInstructionGroup(it)
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
fun AddBlockPanel(blocks: List<SingleInstructionType>, onClick: (SingleInstructionType) -> Unit) {
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

@Composable
fun AddGroupPanel(blocks: List<InstructionGroupType> = InstructionGroupType.entries, onClick: (InstructionGroupMetaData) -> Unit) {
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
                        onClick(type.init())
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
    BuilderTopBar(onAddSingleInstruction = {}, onExecute = {}, onHide = {},
        mode = TopBarMode.ADD_INSTRUCTION,
        supportedInstructions = listOf(),
        enableFieldList = listOf(),
        onEnableField = {}, onBack = {}, onShare = {}, onAddInstructionGroup = {}, onOpenAssets = {}, projectName = "")
}