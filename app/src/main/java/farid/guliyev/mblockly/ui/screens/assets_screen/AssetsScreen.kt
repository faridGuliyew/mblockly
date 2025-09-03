package farid.guliyev.mblockly.ui.screens.assets_screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farid.guliyev.mblockly.R
import farid.guliyev.mblockly.ui.components.button.AppIconButtonBackgrounded
import farid.guliyev.mblockly.ui.components.CustomTextField
import coil.compose.AsyncImage
import farid.guliyev.mblockly.ui.theme.AccentEmerald
import farid.guliyev.mblockly.ui.theme.BackgroundPrimary
import farid.guliyev.mblockly.ui.theme.BackgroundSecondary
import farid.guliyev.mblockly.ui.theme.ErrorRed
import farid.guliyev.mblockly.ui.theme.InfoBlue
import farid.guliyev.mblockly.ui.theme.NeutralGray200
import farid.guliyev.mblockly.ui.theme.NeutralGray400
import farid.guliyev.mblockly.ui.theme.NeutralGray500
import farid.guliyev.mblockly.ui.theme.NeutralGray700
import farid.guliyev.mblockly.ui.theme.PrimaryBlue

@Composable
fun AssetsScreen(
    viewModel: AssetsViewModel,
    state: AssetsState
) {
    val context = LocalContext.current
    
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        viewModel.uploadImage(context, uri)
    }
    
    val audioPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        viewModel.uploadAudio(context, uri)
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AssetsTopBar(
                projectName = viewModel.projectName,
                onBack = viewModel::goBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Upload Section
            item {
                UploadSection(
                    onUploadImage = { imagePickerLauncher.launch(viewModel.getImagePickerIntent()) },
                    onUploadAudio = { audioPickerLauncher.launch(viewModel.getAudioPickerIntent()) }
                )
            }

            // Images Section
            item {
                AssetsSection(
                    title = "Images",
                    icon = Icons.Default.AccountCircle,
                    iconColor = PrimaryBlue
                )
            }

            items(state.images) { image ->
                AssetItemCard(
                    name = image.name,
                    size = image.size,
                    type = AssetType.IMAGE,
                    filePath = image.filePath,
                    onDelete = { viewModel.deleteImage(context, image.id) },
                    onEdit = { viewModel.renameAsset(image.name, image.id) }
                )
            }

            // Audio Section
            item {
                AssetsSection(
                    title = "Audio Files",
                    icon = Icons.Default.AccountCircle,
                    iconColor = AccentEmerald
                )
            }

            items(state.audioFiles) { audio ->
                AssetItemCard(
                    name = audio.name,
                    size = audio.size,
                    type = AssetType.AUDIO,
                    filePath = audio.filePath,
                    onDelete = { viewModel.deleteAudio(context, audio.id) },
                    onEdit = { viewModel.renameAsset(audio.name, audio.id) }
                )
            }

            // Empty state
            if (state.images.isEmpty() && state.audioFiles.isEmpty()) {
                item {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
fun AssetsTopBar(projectName: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(BackgroundPrimary)
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButtonBackgrounded(
                icon = Icons.Default.KeyboardArrowLeft,
                color = NeutralGray700,
                onClick = onBack
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Assets - $projectName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralGray700
            )
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = NeutralGray200.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun UploadSection(
    onUploadImage: () -> Unit,
    onUploadAudio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BackgroundSecondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Upload Assets",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeutralGray700
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UploadButton(
                icon = ImageVector.vectorResource(R.drawable.ic_image),
                text = "Upload Image",
                backgroundColor = PrimaryBlue,
                onClick = onUploadImage,
                modifier = Modifier.weight(1f)
            )

            UploadButton(
                icon = ImageVector.vectorResource(R.drawable.ic_audio),
                text = "Upload Audio",
                backgroundColor = AccentEmerald,
                onClick = onUploadAudio,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun UploadButton(
    icon: ImageVector,
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AssetsSection(
    title: String,
    icon: ImageVector,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeutralGray700
        )
    }
}

@Composable
fun AssetItemCard(
    name: String,
    size: String,
    type: AssetType,
    filePath: String? = null,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle asset selection */ },
        colors = CardDefaults.cardColors(
            containerColor = BackgroundPrimary
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Asset preview/icon
            when (type) {
                AssetType.IMAGE -> {
                    if (filePath != null) {
                        AsyncImage(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = filePath,
                            contentDescription = name
                        )
                    } else {
                        AppIconButtonBackgrounded(
                            icon = ImageVector.vectorResource(type.icon),
                            color = type.color,
                            onClick = {}
                        )
                    }
                }
                AssetType.AUDIO -> {
                    AppIconButtonBackgrounded(
                        icon = ImageVector.vectorResource(type.icon),
                        color = type.color,
                        onClick = {}
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Asset info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = NeutralGray700
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${type.name.lowercase().replaceFirstChar { it.uppercase() }} • $size",
                    fontSize = 14.sp,
                    color = NeutralGray500
                )
            }

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Edit button
                AppIconButtonBackgrounded(
                    icon = Icons.Default.Edit,
                    color = InfoBlue,
                    onClick = onEdit
                )
                
                // Delete button
                AppIconButtonBackgrounded(
                    icon = Icons.Default.Delete,
                    color = ErrorRed,
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "No assets",
            tint = NeutralGray400,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No assets uploaded yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = NeutralGray500
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Upload images and audio files to use them in your projects",
            fontSize = 14.sp,
            color = NeutralGray400,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AssetsScreenPreview() {
    val mockState = AssetsState(
        images = listOf(
            AssetItem("img_1", "background.jpg", "2.3 MB", AssetType.IMAGE, null),
            AssetItem("img_2", "logo.png", "156 KB", AssetType.IMAGE, null)
        ),
        audioFiles = listOf(
            AssetItem("audio_1", "background_music.mp3", "4.7 MB", AssetType.AUDIO, null),
            AssetItem("audio_2", "sound_effect.wav", "892 KB", AssetType.AUDIO, null)
        )
    )
    
//    AssetsScreen(
//        viewModel = AssetsViewModel(),
//        state = mockState
//    )
}
