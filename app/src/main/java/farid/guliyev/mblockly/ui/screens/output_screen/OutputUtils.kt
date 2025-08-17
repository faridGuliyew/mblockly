package farid.guliyev.mblockly.ui.screens.output_screen

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import farid.guliyev.mblockly.domain.UNDEFINED_VARIABLE
import farid.guliyev.mblockly.domain.model.InstructionField

fun InstructionField<Float, *>.extractValue(
    floatVariables: Map<String, MutableFloatState>
) : MutableFloatState {
    val value = this.value
    return value.toFloatOrNull()?.run { mutableFloatStateOf(this) } ?: floatVariables[value] ?: error(UNDEFINED_VARIABLE.format(value))
}