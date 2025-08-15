package farid.guliyev.mblockly.ui.screens.builder_screen

import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.InstructionType
import farid.guliyev.mblockly.domain.model.init
import farid.guliyev.mblockly.ui.model.DraggableUiBlock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class BuilderViewModel {
    val state = MutableStateFlow(BuilderState())


    fun addInstruction(type: InstructionType) {
        state.update { it.copy(draggableUiBlocks = it.draggableUiBlocks + DraggableUiBlock(type.init())) }
    }

    fun editInstruction(newInstruction: Instruction, index: Int) {
        state.update {
            val oldBlock = it.draggableUiBlocks[index]
            val newBlock = oldBlock.copy(instruction = newInstruction)
            val updatedBlocks = it.draggableUiBlocks.toMutableList().apply { set(index, newBlock) }
            it.copy(draggableUiBlocks = updatedBlocks)
        }
    }

    fun removeInstruction(index: Int) {
        state.update { it.copy(draggableUiBlocks = it.draggableUiBlocks.toMutableList().apply { removeAt(index) })}
    }

    fun moveInstructionUp(index: Int) {
        if (index == 0) return

        state.update {
            val thisBlock = it.draggableUiBlocks[index]
            val aboveBlock =it.draggableUiBlocks[index - 1]
            val updatedBlocks = it.draggableUiBlocks.toMutableList().apply {
                set(index - 1, thisBlock)
                set(index, aboveBlock)
            }
            it.copy(draggableUiBlocks = updatedBlocks)
        }
    }

    fun moveInstructionDown(index: Int) {
        if (index == state.value.draggableUiBlocks.lastIndex) return

        state.update {
            val thisBlock = it.draggableUiBlocks[index]
            val belowBlock =it.draggableUiBlocks[index + 1]
            val updatedBlocks = it.draggableUiBlocks.toMutableList().apply {
                set(index + 1, thisBlock)
                set(index, belowBlock)
            }
            it.copy(draggableUiBlocks = updatedBlocks)
        }
    }
}