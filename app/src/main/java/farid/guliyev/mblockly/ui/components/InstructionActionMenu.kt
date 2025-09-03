package farid.guliyev.mblockly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.R
import farid.guliyev.mblockly.ui.components.button.AppIconButton
import farid.guliyev.mblockly.ui.theme.BackgroundSecondary
import farid.guliyev.mblockly.ui.theme.ErrorRed
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.PrimaryBlue
import farid.guliyev.mblockly.ui.theme.SuccessGreen

@Composable
fun InstructionActionMenu(
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveOut: () -> Unit,
    onMoveIn: () -> Unit,
    onDuplicate: (() -> Unit)? = null,
    onEditSeparately: (() -> Unit)? = null,
    onToggleActive: () -> Unit,
    onRemove: () -> Unit,
    onToggleMinimize: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        AppIconButton(
            onClick = { expanded = true },
            icon = Icons.Default.MoreVert,
            color = NeutralGray700
        )

        DropdownMenu(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundSecondary),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Move up
            DropdownMenuItem(
                text = {
                    DropdownItemContent("Move up", Icons.Default.KeyboardArrowUp, PrimaryBlue)
                },
                onClick = {
                    onMoveUp()
                    expanded = false
                }
            )

            // Move down
            DropdownMenuItem(
                text = {
                    DropdownItemContent("Move down", Icons.Default.KeyboardArrowDown, PrimaryBlue)
                },
                onClick = {
                    onMoveDown()
                    expanded = false
                }
            )

            // Move out (long press hint)
            DropdownMenuItem(
                text = {
                    DropdownItemContent("Move out of current group", Icons.Default.KeyboardArrowUp, PrimaryBlue)
                },
                onClick = {
                    onMoveOut()
                    expanded = false
                }
            )

            // Move in (long press hint)
            DropdownMenuItem(
                text = {
                    DropdownItemContent("Move inside below group", Icons.Default.KeyboardArrowDown, PrimaryBlue)
                },
                onClick = {
                    onMoveIn()
                    expanded = false
                }
            )

            // Duplicate
            onDuplicate?.let {
                DropdownMenuItem(
                    text = {
                        DropdownItemContent("Duplicate", ImageVector.vectorResource(R.drawable.ic_copy), PrimaryBlue)
                    },
                    onClick = {
                        onDuplicate()
                        expanded = false
                    }
                )
            }

            // Edit separately
            onEditSeparately?.let {
                DropdownMenuItem(
                    text = {
                        DropdownItemContent("Focus mode", Icons.Default.ExitToApp, SuccessGreen)
                    },
                    onClick = {
                        onEditSeparately()
                        expanded = false
                    }
                )
            }

            // Toggle active
            DropdownMenuItem(
                text = {
                    DropdownItemContent("Toggle active", Icons.Default.Build, NeutralGray700)
                },
                onClick = {
                    onToggleActive()
                    expanded = false
                }
            )

            // Remove
            DropdownMenuItem(
                text = {
                    DropdownItemContent("Remove", Icons.Default.Close, ErrorRed)
                },
                onClick = {
                    onRemove()
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun DropdownItemContent(
    title: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = title,
            tint = color
        )
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = NeutralGray700
        )
    }
}