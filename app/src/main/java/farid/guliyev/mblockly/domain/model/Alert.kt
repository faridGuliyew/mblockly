package farid.guliyev.mblockly.domain.model

data class Alert(
    val title: String,
    val description: String,
    val type: AlertType
)