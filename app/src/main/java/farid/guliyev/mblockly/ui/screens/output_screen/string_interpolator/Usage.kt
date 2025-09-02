package farid.guliyev.mblockly.ui.screens.output_screen.string_interpolator

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf

fun main() {
    val input = "Health: \$hptt"
    input.interpolate(mapOf("hptt" to mutableFloatStateOf(31f))).also {
        println(it.value)
    }
}

fun String.interpolate(
    floatVariables: Map<String, MutableFloatState>
) : State<String> {
    val regex = """(?<!\w)\$\w+""".toRegex()
    return derivedStateOf {
        replace(regex) {
            val floatVariable = floatVariables[it.value.substring(1)]
            floatVariable?.run { this.value.toString() } ?: it.value
        }
    }
}