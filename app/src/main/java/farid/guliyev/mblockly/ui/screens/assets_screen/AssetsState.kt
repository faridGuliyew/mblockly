package farid.guliyev.mblockly.ui.screens.assets_screen

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
    val type: AssetType
)

