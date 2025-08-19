package farid.guliyev.mblockly.ui.navigation

import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderState
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data object HomeRoute

@Serializable
data class BuilderRoute(val instructionGroupEncoded: String = "") {
    val instructionGroup get() = runCatching { Json.decodeFromString<InstructionBlock.InstructionGroup>(instructionGroupEncoded) }.getOrNull()
}