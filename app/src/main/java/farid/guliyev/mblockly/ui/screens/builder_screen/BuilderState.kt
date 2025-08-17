package farid.guliyev.mblockly.ui.screens.builder_screen

import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.domain.model.optionalFields
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel.Companion.MAIN_GROUP_NAME
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel.Companion.ROOT
import farid.guliyev.mblockly.ui.screens.builder_screen.components.TopBarMode
import java.util.UUID

class BuilderState (
    val mainInstructionGroup: InstructionBlock.InstructionGroup = InstructionBlock.InstructionGroup(id = MAIN_GROUP_NAME, parentId = ROOT)
) {
    fun copy(mainInstructionGroup: InstructionBlock.InstructionGroup = this.mainInstructionGroup) : BuilderState {
        return BuilderState(
            mainInstructionGroup = mainInstructionGroup
        )
    }
}

data class BuilderSubState(
    val topBarMode: TopBarMode = TopBarMode.HIDDEN,
    val addSingleInstructionParentId: String? = null,
    val enableOptionalFieldInstructionAndIndex : Pair<InstructionBlock.SingleInstruction, Int>? = null,
    val focusedEditInstructionGroupsWithIndices : List<Pair<InstructionBlock.InstructionGroup, Int>> = emptyList()
) {
    val enableOptionalFieldList = enableOptionalFieldInstructionAndIndex?.first?.instruction?.optionalFields.orEmpty()
}

sealed class InstructionBlock {
    abstract val parentId: String
    abstract val isMinimized: Boolean
    abstract fun copy(parentId: String = this.parentId, isMinimized: Boolean = this.isMinimized) : InstructionBlock

    data class SingleInstruction(
        val instruction: Instruction,
        override val parentId: String,
        override val isMinimized: Boolean = false
    ) : InstructionBlock() {
        override fun copy(parentId: String, isMinimized: Boolean) : SingleInstruction {
            return this.copy(instruction = instruction, parentId = parentId, isMinimized = isMinimized)
        }
    }

    data class InstructionGroup(
        val instructionBlocks: MutableList<InstructionBlock> = mutableListOf(),
        val id : String = UUID.randomUUID().toString(),
        override val parentId: String,
        override val isMinimized: Boolean = false
    ) : InstructionBlock() {
        override fun copy(parentId: String, isMinimized: Boolean) : InstructionGroup {
            return this.copy(id = id, parentId = parentId, isMinimized = isMinimized)
        }
    }
}