package farid.guliyev.mblockly.ui.screens.output_screen

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import farid.guliyev.mblockly.domain.UNDEFINED_VARIABLE
import farid.guliyev.mblockly.domain.model.instruction.InstructionField

fun String.extractValue(
    floatVariables: Map<String, MutableFloatState>
) : MutableFloatState {
    return toFloatOrNull()?.run { mutableFloatStateOf(this) } ?: floatVariables[this] ?: error(UNDEFINED_VARIABLE.format(this))
}