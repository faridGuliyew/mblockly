package farid.guliyev.mblockly.ui.screens.builder_screen

import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.exception_handling.AppException
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.domain.BLOCK_CANNOT_BE_MOVED_FURTHER
import farid.guliyev.mblockly.domain.NO_SUCH_PARENT_WITH_GROUP_ID
import farid.guliyev.mblockly.domain.TARGET_BLOCK_IS_NOT_GROUP
import farid.guliyev.mblockly.domain.model.ExceptionType
import farid.guliyev.mblockly.domain.model.InstructionType
import farid.guliyev.mblockly.domain.model.init
import farid.guliyev.mblockly.ui.screens.builder_screen.components.TopBarMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class BuilderViewModel : BaseViewModel() {

    companion object {
        const val ROOT = "ROOT"
        const val MAIN_GROUP_NAME = "MAIN"
    }

    val state = MutableStateFlow(BuilderState())
    private val instructionGroupsById = mutableMapOf(MAIN_GROUP_NAME to state.value.mainInstructionGroup)

    val subState = MutableStateFlow(BuilderSubState())

    /** ===== MAIN SECTION - Below functions only interact with state. NOT subState. ===== */

    fun updateInstruction(index: Int, newInstructionBlock: InstructionBlock) {

        // MAIN GROUP CANNOT BE MODIFIED
        if (newInstructionBlock.parentId == ROOT) return

        runSafelyInBg {
            newInstructionBlock.getParent().instructionBlocks[index] = newInstructionBlock
        }

    }

    fun removeInstructionBlock(index: Int, block: InstructionBlock) {
        runSafelyInBg {
            block.getParent().instructionBlocks.removeAt(index)
        }
    }

    fun moveInstructionOut(index: Int, block: InstructionBlock) {
        runSafelyInBg {
            val parentGroup = block.getParent()
            val grandParentGroup = parentGroup.getParent()

            block.changeParent(newParent = grandParentGroup, currentIndex = index, newIndex = 0)
        }
    }

    fun moveInstructionIn(index: Int, block: InstructionBlock) {
        runSafelyInBg {
            val parentGroup = block.getParent()

            val groupToMoveInside = (parentGroup.instructionBlocks.getOrNull(index + 1) as? InstructionBlock.InstructionGroup)
            if (groupToMoveInside == null) failGracefully(message = TARGET_BLOCK_IS_NOT_GROUP, ExceptionType.WARNING)

            block.changeParent(newParent = groupToMoveInside, currentIndex = index, newIndex = 0)
        }
    }

    fun moveInstructionUp(index: Int, block: InstructionBlock) {
        runSafelyInBg {
            reorderInstructionInParent(currentIndex = index, block = block, newIndex = index - 1)
        }
    }

    fun moveInstructionDown(index: Int, block: InstructionBlock) {
        runSafelyInBg {
            reorderInstructionInParent(currentIndex = index, block = block, newIndex = index + 1)
        }
    }

    /** ==== Below functions PRIMARILY interact with STATE. MAY OR MAY NOT interact with main substate ==== */

    fun addSingleInstruction(type: InstructionType) {
        runSafelyInBg {
            val parentId = subState.value.addSingleInstructionParentId ?: return@runSafelyInBg
            addInstructionBlock(newBlock = InstructionBlock.SingleInstruction(instruction = type.init(), parentId = parentId))
        }
    }

    fun addInstructionGroup(parentId: String) {
        runSafelyInBg {
            val newGroup = InstructionBlock.InstructionGroup(parentId = parentId)
            addInstructionBlock(newBlock = newGroup)
            instructionGroupsById[newGroup.id] = newGroup
        }
    }

    fun enableInstructionOptionalField(fieldName: String) {
        val instruction = subState.value.enableOptionalFieldInstructionAndIndex?.first ?: return

        val newInstruction = instruction.copy(instruction = instruction.instruction.changeOptionalFieldByName(fieldName, isEnabled = true))
        val selectedInstructionIndex = subState.value.enableOptionalFieldInstructionAndIndex?.second ?: -1

        updateInstruction(selectedInstructionIndex, newInstruction)
    }

    /** === BELOW FUNCTIONS ARE FOR REPETITIVE TASKS - THEY NEVER HANDLE EXCEPTIONS IMPLICITLY!  === */

    /** WARNING: IMPURE FUNCTIONS */
    @Throws(AppException::class)
    private fun reorderInstructionInParent(currentIndex: Int, block: InstructionBlock, newIndex: Int) {
        val parentGroup = block.getParent()
        if (newIndex !in parentGroup.instructionBlocks.indices) failGracefully(BLOCK_CANNOT_BE_MOVED_FURTHER, ExceptionType.WARNING)

        val blockAtTargetIndex = parentGroup.instructionBlocks[newIndex]
        parentGroup.instructionBlocks.apply {
            set(newIndex, block)
            set(currentIndex, blockAtTargetIndex)
        }
    }

    @Throws(AppException::class)
    private fun InstructionBlock.getParent() : InstructionBlock.InstructionGroup {
        val parent = instructionGroupsById[parentId] ?: failGracefully(message = NO_SUCH_PARENT_WITH_GROUP_ID.format(parentId))
        return parent
    }

    @Throws(AppException::class)
    private fun InstructionBlock.changeParent(newParent: InstructionBlock.InstructionGroup, currentIndex: Int, newIndex: Int) {
        val currentParent = getParent()
        currentParent.instructionBlocks.removeAt(currentIndex)
        newParent.instructionBlocks.add(newIndex, this.copy(parentId = newParent.id))
    }

    @Throws(AppException::class)
    private fun addInstructionBlock(newBlock: InstructionBlock) {
        newBlock.getParent().instructionBlocks.add(newBlock)
    }

    /** PURE FUNCTIONS */


    /** ====== SUB SECTION =========*/


    /** Below functions ONLY interact with subState */

    fun showAddSingleInstructionOptions(parentId: String) {
        subState.update { it.copy(topBarMode = TopBarMode.ADD_INSTRUCTION, addSingleInstructionParentId = parentId) }
    }

    fun showEnableSingleInstructionOptionalField(index: Int, block: InstructionBlock.SingleInstruction) {
        subState.update { it.copy(topBarMode = TopBarMode.ENABLE_FIELD, enableOptionalFieldInstructionAndIndex = block to index) }
    }

    fun enableFocusEditingForGroup(index: Int, group: InstructionBlock.InstructionGroup) {
        subState.update { it.copy(focusedEditInstructionGroupsWithIndices = it.focusedEditInstructionGroupsWithIndices + (group to index)) }
    }

    fun exitFocusEditingForGroup() {
        subState.update { it.copy(focusedEditInstructionGroupsWithIndices = it.focusedEditInstructionGroupsWithIndices.toMutableList().apply { removeLastOrNull() }) }
    }

    fun hideTopBarMenu() {
        subState.update { it.copy(topBarMode = TopBarMode.HIDDEN) }
    }

    /** Below functions primarily interact with subState. MAY OR MAY NOT interact with main state */
}