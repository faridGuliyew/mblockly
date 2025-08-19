package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.runtime.snapshots.SnapshotStateList
import farid.guliyev.mblockly.core.serialization.InstructionRuntimeSerializer
import farid.guliyev.mblockly.core.serialization.SnapshotStateListSerializer
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.domain.model.instruction.optionalFields
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel.Companion.initialGroup
import farid.guliyev.mblockly.ui.screens.builder_screen.components.TopBarMode
import kotlinx.serialization.Serializable
import java.util.UUID

class BuilderState (
    val mainInstructionGroup: InstructionBlock.InstructionGroup = initialGroup
) {
    fun copy(mainInstructionGroup: InstructionBlock.InstructionGroup = this.mainInstructionGroup) : BuilderState {
        return BuilderState(
            mainInstructionGroup = mainInstructionGroup
        )
    }
}

data class BuilderSubState(
    val topBarMode: TopBarMode = TopBarMode.HIDDEN,
    val sheetType: SheetType = SheetType.HIDDEN,
    val addSingleInstructionParentId: String? = null,
    val enableOptionalFieldInstructionAndIndex : Pair<InstructionBlock.SingleInstruction, Int>? = null,
    val focusedEditInstructionGroupsWithIndices : List<Pair<InstructionBlock.InstructionGroup, Int>> = emptyList()
) {
    val enableOptionalFieldList = enableOptionalFieldInstructionAndIndex?.first?.instruction?.base?.optionalFields.orEmpty()
}

@Serializable
sealed class InstructionBlock {
    abstract val parentId: String
    abstract val isMinimized: Boolean
    abstract fun copy(parentId: String = this.parentId, isMinimized: Boolean = this.isMinimized) : InstructionBlock

    @Serializable
    data class SingleInstruction(
        @Serializable(with = InstructionRuntimeSerializer::class)
        val instruction: InstructionRuntime,
        override val parentId: String,
        override val isMinimized: Boolean = false
    ) : InstructionBlock() {
        override fun copy(parentId: String, isMinimized: Boolean) : SingleInstruction {
            return this.copy(instruction = instruction, parentId = parentId, isMinimized = isMinimized)
        }
    }

    @Serializable
    data class InstructionGroup(
        @Serializable(with = SnapshotStateListSerializer::class)
        val instructionBlocks: SnapshotStateList<InstructionBlock> = SnapshotStateList(),
        val id : String = UUID.randomUUID().toString(),
        override val parentId: String,
        override val isMinimized: Boolean = false
    ) : InstructionBlock() {
        override fun copy(parentId: String, isMinimized: Boolean) : InstructionGroup {
            return this.copy(id = id, parentId = parentId, isMinimized = isMinimized)
        }
    }
}