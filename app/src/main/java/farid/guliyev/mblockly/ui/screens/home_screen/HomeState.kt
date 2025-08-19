package farid.guliyev.mblockly.ui.screens.home_screen

data class HomeState(
    val projectFiles: List<SavedFile> = emptyList(),
)

data class SavedFile(
    val name: String,
    val lastModified: String
)