package farid.guliyev.mblockly.ui.screens.builder_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.R
import farid.guliyev.mblockly.ui.components.CustomTextField
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgroundedWithText
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.PrimaryBlue
import farid.guliyev.mblockly.ui.theme.SuccessGreen

enum class ShareSheetOption (
    val label: String,
    val color: Color,
    @DrawableRes val icon: Int
) {
    SAVE("Save", PrimaryBlue, R.drawable.ic_save),
    SHARE("Share", SuccessGreen, R.drawable.ic_share)
}

@Composable
fun SaveAndShareBottomSheetContent(
    onDismiss: () -> Unit,
    onSave: (projectName: String) -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Choose an action",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NeutralGray700
        )

        var selectedOption by remember { mutableStateOf<ShareSheetOption?>(null) }

        Row (horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShareSheetOption.entries.forEach {
                AppIconButtonBackgroundedWithText(
                    modifier = Modifier.weight(1F),
                    icon = ImageVector.vectorResource(it.icon),
                    text = it.label,
                    color = it.color,
                    isHighlighted = it == selectedOption,
                    onClick = {
                        if (it == ShareSheetOption.SHARE) onShare()

                        selectedOption = it
                    }
                )
            }
        }

        AnimatedContent(targetState = selectedOption) {
            when(it) {
                ShareSheetOption.SAVE -> {
                    var fileName by remember { mutableStateOf("my_great_program") }
                    Column (verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Specify project name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeutralGray700
                        )
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row (
                                modifier = Modifier.weight(1F),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                CustomTextField(
                                    value = fileName,
                                    onValueChange = { fileName = it }
                                )
                            }

                            TextButton(onClick = {
                                onDismiss()
                                onSave(fileName)
                            }) {
                                Text(
                                    text = "Confirm save",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ShareSheetOption.SAVE.color
                                )
                            }
                        }
                    }
                }
                ShareSheetOption.SHARE -> Unit
                null -> Unit
            }
        }
    }
}