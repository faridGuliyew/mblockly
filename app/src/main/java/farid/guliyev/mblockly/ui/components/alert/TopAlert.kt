package farid.guliyev.mblockly.ui.components.alert

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import farid.guliyev.mblockly.ui.extensions.containerColor
import farid.guliyev.mblockly.ui.extensions.contentColor
import farid.guliyev.mblockly.ui.theme.NeutralGray800
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun TopAlert(
    alert: Alert?,
    onDismiss: () -> Unit
) {
    val shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
    val animationDuration = 1000

    var isAlertVisible by remember(alert) { mutableStateOf(alert != null) }

    LaunchedEffect(isAlertVisible) {
        if (!isAlertVisible) {
            delay(animationDuration.toLong())
            onDismiss()
        } else {
            delay(5.seconds)
            isAlertVisible = false
        }
    }

    AnimatedVisibility(
        visible = isAlertVisible,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(animationDuration / 2)) + fadeIn(tween(animationDuration / 2)),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(animationDuration)) + fadeOut(tween(animationDuration))
    ) {
        val current = alert ?: return@AnimatedVisibility

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(color = Color.White)
                .background(
                    color = current.type.containerColor,
                    shape = shape
                )
                .border(width = 1.dp, color = NeutralGray800, shape = shape)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .statusBarsPadding()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Title + description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = current.title,
                        color = current.type.contentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = current.description,
                        color = current.type.contentColor.copy(alpha = 0.9f),
                        fontSize = 15.sp
                    )
                }

                // Dismiss button
                IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            current.type.contentColor.copy(alpha = 0.1f)
                        ),
                    onClick = { isAlertVisible = false }
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = current.type.contentColor
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun TopAlertPrev() {
    Column {
        AlertType.entries.forEach {
            TopAlert(
                alert = Alert("Title", "description", it),
                onDismiss = {}
            )
        }
    }
}