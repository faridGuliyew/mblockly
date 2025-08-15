package farid.guliyev.mblockly.ui.screens.builder_screen

import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.ui.model.DraggableUiBlock

data class BuilderState (
    val draggableUiBlocks: List<DraggableUiBlock<Instruction>> = emptyList()
)