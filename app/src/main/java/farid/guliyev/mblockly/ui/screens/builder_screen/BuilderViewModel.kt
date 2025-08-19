package farid.guliyev.mblockly.ui.screens.builder_screen

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import farid.guliyev.mblockly.core.base.BaseViewModel
import farid.guliyev.mblockly.core.exception_handling.AppException
import farid.guliyev.mblockly.core.exception_handling.failGracefully
import farid.guliyev.mblockly.di.NavigationController
import farid.guliyev.mblockly.di.NavigationModule
import farid.guliyev.mblockly.domain.BLOCK_CANNOT_BE_MOVED_FURTHER
import farid.guliyev.mblockly.domain.NO_SUCH_PARENT_WITH_GROUP_ID
import farid.guliyev.mblockly.domain.TARGET_BLOCK_IS_NOT_GROUP
import farid.guliyev.mblockly.domain.model.ExceptionType
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import farid.guliyev.mblockly.domain.model.instruction.InstructionType
import farid.guliyev.mblockly.domain.model.instruction.init
import farid.guliyev.mblockly.ui.components.sheet.SheetType
import farid.guliyev.mblockly.ui.navigation.BuilderRoute
import farid.guliyev.mblockly.ui.screens.builder_screen.components.TopBarMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class BuilderViewModel (
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navigationController: NavigationController = NavigationModule.navController

    companion object {
        const val ROOT = "ROOT"
        const val MAIN_GROUP_NAME = "MAIN"

        val initialGroup get() = InstructionBlock.InstructionGroup(id = MAIN_GROUP_NAME, parentId = ROOT)
    }

    private val groupFromArgs = savedStateHandle.toRoute<BuilderRoute>().instructionGroup
    val state = MutableStateFlow(BuilderState(mainInstructionGroup = groupFromArgs ?: initialGroup))
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
        runSafelyInBg {
            val block = subState.value.enableOptionalFieldInstructionAndIndex?.first ?: return@runSafelyInBg

            val updatedBase = block.instruction.base.changeOptionalFieldByName(fieldName, isEnabled = true)
            val newInstruction = InstructionRuntime.fromBase(updatedBase)
            val selectedInstructionIndex = subState.value.enableOptionalFieldInstructionAndIndex?.second ?: -1

            updateInstruction(selectedInstructionIndex, block.copy(instruction = newInstruction))
        }
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

    fun showShareSheet() { showSheet(SheetType.SHARE) }

    /** Below functions primarily interact with subState. MAY OR MAY NOT interact with main state */

    /** Below functions do not interact with state at all */
    fun saveToFile(context: Context, fileName: String) {
        runSafelyInBg {
            val file = File(context.filesDir, "$fileName.mb")
            val isFileCreated = file.createNewFile()
            if (!isFileCreated) {
                showConfirmation(description = "This file already exists, do you want to override it?")
            }

            // Write project into file
            saveCurrentStateToFile(file)

            showSuccessAlert(message = "File named: $fileName saved successfully!")
        }
    }

    fun shareFile(context: Context) {
        runSafelyInBg {
            val file = File(context.filesDir, "shared_project.mb")
            saveCurrentStateToFile(file)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Check out this project!")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share project using"))
        }
    }

    private fun saveCurrentStateToFile(file: File) {
        val json = Json.encodeToString(state.value.mainInstructionGroup)
        file.outputStream().buffered().use { it.write(json.toByteArray()) }
    }

    fun goBack(context: Context) {
        runSafelyInBg {
            val file = File(context.filesDir, "most_recent_project.mb")
            saveCurrentStateToFile(file)
            navigationController.sendCommand { popBackStack() }
        }
    }
}