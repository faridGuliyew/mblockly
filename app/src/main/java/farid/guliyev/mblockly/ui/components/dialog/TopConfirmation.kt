package farid.guliyev.mblockly.ui.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.domain.model.Alert
import farid.guliyev.mblockly.domain.model.AlertType
import farid.guliyev.mblockly.ui.components.CustomTextField
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgrounded
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgroundedWithText
import farid.guliyev.mblockly.ui.extensions.containerColor
import farid.guliyev.mblockly.ui.extensions.contentColor
import farid.guliyev.mblockly.ui.theme.ErrorRed
import farid.guliyev.mblockly.ui.theme.InfoBlue
import farid.guliyev.mblockly.ui.theme.NeutralGray200
import farid.guliyev.mblockly.ui.theme.NeutralGray600
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.NeutralGray800
import farid.guliyev.mblockly.ui.theme.SuccessGreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

sealed interface Confirmation <T> {

    val title: String get() = "Confirmation"
    val description: String

    data class SimpleConfirmation(
        override val title: String = "Confirmation",
        override val description: String
    ) : Confirmation<Boolean>

    data class InputConfirmation(
        override val title: String = "Confirmation",
        override val description: String
    ) : Confirmation<String>
}

@Composable
fun <T> TopConfirmation(
    confirmation: Confirmation<T>?,
    onDismiss: () -> Unit,
    onConfirm: (T) -> Unit
) {
    val shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)

    AnimatedVisibility(
        visible = confirmation != null,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        val current = confirmation ?: return@AnimatedVisibility

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(color = Color.White)
                .background(
                    color = InfoBlue.copy(0.2F),
                    shape = shape
                )
                .border(width = 1.dp, color = NeutralGray600, shape = shape)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Title + description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = current.title,
                        color = InfoBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = current.description,
                        color = InfoBlue.copy(alpha = 0.9f),
                        fontSize = 15.sp
                    )
                }
            }

            when (confirmation) {
                is Confirmation.SimpleConfirmation -> {
                    ConfirmationActionButtons(
                        onDismiss = onDismiss,
                        onConfirm = { onConfirm(true as T) }
                    )
                }

                is Confirmation.InputConfirmation -> {

                    var fileName by rememberSaveable { mutableStateOf("imported_file") }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CustomTextField(
                            value = fileName,
                            onValueChange = { fileName = it }
                        )
                        Text(
                            text = ".mb",
                            fontSize = 14.sp,
                            color = NeutralGray700
                        )
                    }

                    ConfirmationActionButtons(
                        onDismiss = onDismiss,
                        onConfirm = {
                            onConfirm(fileName as T)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ColumnScope.ConfirmationActionButtons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Row (horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        AppIconButtonBackgroundedWithText(
            modifier = Modifier.weight(1F),
            icon = Icons.Default.Close,
            text = "Dismiss",
            color = ErrorRed,
            isHighlighted = true,
            onClick = onDismiss
        )

        AppIconButtonBackgroundedWithText(
            modifier = Modifier.weight(1F),
            icon = Icons.Default.Done,
            text = "Confirm",
            color = SuccessGreen,
            isHighlighted = true,
            onClick = onConfirm
        )
    }
}


@Preview
@Composable
private fun TopAlertPrev() {
    Column {
        TopConfirmation(
            confirmation = Confirmation.InputConfirmation("Title", "description"),
            onDismiss = {},
            onConfirm = {}
        )
    }
}