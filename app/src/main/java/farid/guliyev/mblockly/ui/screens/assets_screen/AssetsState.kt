package farid.guliyev.mblockly.ui.screens.assets_screen

import androidx.compose.ui.graphics.Color
import farid.guliyev.mblockly.R
import farid.guliyev.mblockly.ui.theme.AccentEmerald
import farid.guliyev.mblockly.ui.theme.PrimaryBlue
import kotlinx.serialization.Serializable

@Serializable
data class AssetsState(
    val images: List<AssetItem> = emptyList(),
    val audioFiles: List<AssetItem> = emptyList()
)

@Serializable
data class AssetItem(
    val id: String,
    val name: String,
    val size: String,
    val type: AssetType,
    val filePath: String? = null
)

enum class AssetType(val icon: Int, val color: Color) {
    IMAGE(R.drawable.ic_image, PrimaryBlue), 
    AUDIO(R.drawable.ic_audio, AccentEmerald)
}

