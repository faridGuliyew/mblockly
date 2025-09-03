package farid.guliyev.mblockly.ui.screens.builder_screen

import androidx.compose.runtime.snapshots.SnapshotStateList
import farid.guliyev.mblockly.core.serialization.InstructionRuntimeSerializer
import farid.guliyev.mblockly.core.serialization.SnapshotStateListSerializer
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.domain.model.instruction.optionalFields
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import farid.guliyev.mblockly.ui.screens.builder_screen.BuilderViewModel.Companion.initialGroup
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionGroupMetaData.*
import farid.guliyev.mblockly.ui.screens.builder_screen.components.TopBarMode
import kotlinx.serialization.SerialName
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
    val projectName: String,
    val addSingleInstructionParentId: String? = null,
    val addInstructionGroupParentId: String? = null,
    val enableOptionalFieldInstructionAndIndex : Pair<InstructionBlock.SingleInstruction, Int>? = null,
    val focusedEditInstructionGroupsWithIndices : List<Pair<InstructionBlock.InstructionGroup, Int>> = emptyList()
) {
    val enableOptionalFieldList = enableOptionalFieldInstructionAndIndex?.first?.instruction?.base?.optionalFields.orEmpty()
}

@Serializable
sealed class InstructionBlock {
    abstract val parentId: String
    abstract val isMinimized: Boolean
    abstract val isActive: Boolean
    abstract fun copy(parentId: String = this.parentId, isMinimized: Boolean = this.isMinimized) : InstructionBlock

    /** WARNING: Performance hit can be significant, if block is huge. */
    fun hardCopy(): InstructionBlock {
        return when (this) {
            is SingleInstruction -> {
                SingleInstruction(
                    instruction = instruction,
                    parentId = parentId,
                    isMinimized = isMinimized
                )
            }
            is InstructionGroup -> {
                InstructionGroup(
                    instructionBlocks = SnapshotStateList<InstructionBlock>().also { list ->
                        this.instructionBlocks.forEach { block -> list.add(block.hardCopy()) } // recursively deep copy
                    },
                    metaData = metaData,
                    parentId = parentId,
                    isMinimized = isMinimized
                )
            }
        }
    }

    @Serializable
    data class SingleInstruction(
        @Serializable(with = InstructionRuntimeSerializer::class)
        val instruction: InstructionRuntime,
        override val parentId: String,
        override val isMinimized: Boolean = false,
        override val isActive: Boolean = true
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
        val metaData: InstructionGroupMetaData,
        override val parentId: String,
        override val isMinimized: Boolean = false,
        override val isActive: Boolean = true
    ) : InstructionBlock() {
        override fun copy(parentId: String, isMinimized: Boolean) : InstructionGroup {
            return this.copy(id = id, parentId = parentId, isMinimized = isMinimized)
        }
    }
}

@Serializable
sealed interface InstructionGroupMetaData {
    @SerialName("meta_data_type")
    val groupType: InstructionGroupType

    @Serializable
    data object Thread: InstructionGroupMetaData {
        override val groupType: InstructionGroupType = InstructionGroupType.THREAD
    }

    @Serializable
    data object InfiniteLoop: InstructionGroupMetaData {
        override val groupType: InstructionGroupType = InstructionGroupType.INFINITE_LOOP
    }

    @Serializable
    data class FiniteLoop (val loopCount: String): InstructionGroupMetaData {
        override val groupType: InstructionGroupType = InstructionGroupType.FINITE_LOOP
    }

    @Serializable
    data class Conditional(val condition: String): InstructionGroupMetaData {
        override val groupType: InstructionGroupType = InstructionGroupType.CONDITIONAL
    }
}

enum class InstructionGroupType (val description: String, val label: String) {
    THREAD(description = "💾 Add a new thread for parallel execution", label = "Thread"),
    INFINITE_LOOP(description = "😴 Add an infinite loop", label = "Infinite loop"),
    FINITE_LOOP(description = "😴 Add a finite loop", label = "Finite loop"),
    CONDITIONAL(description = "🧩 Add a conditional check", label = "Conditional")
}

fun InstructionGroupType.init() : InstructionGroupMetaData {
    return when(this) {
        InstructionGroupType.THREAD -> Thread
        InstructionGroupType.INFINITE_LOOP -> InfiniteLoop
        InstructionGroupType.FINITE_LOOP -> FiniteLoop("1")
        InstructionGroupType.CONDITIONAL -> Conditional("")
    }
}