package farid.guliyev.mblockly.ui.screens.output_screen

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import farid.guliyev.mblockly.domain.UNDEFINED_VARIABLE

fun String.getVariableState(floatVariables: Map<String, MutableFloatState>) : MutableFloatState {
    return floatVariables[this] ?: error(UNDEFINED_VARIABLE.format(this))
}
fun String.extractValue(
    floatVariables: Map<String, MutableFloatState>
) : State<Float> {
    val tokens = this.extractTokens()
    return tokens.extractFloatValue(floatVariables)
}

fun String.extractTokens() : List<InstructionToken> {
    val parts = this.split(" ").filterNot { it.isEmpty() }
    val tokens = parts.map { part->
        when {
            part in setOf("+", "-", "*", "/") -> InstructionToken.MATH_OPERATION(MathOperationType.parse(part))
            part in setOf("(", ")") -> InstructionToken.PARANTHESIS(ParanthesisType.parse(part))
            part.toFloatOrNull() != null -> InstructionToken.FLOAT(part.toFloat())
            else -> InstructionToken.VARIABLE(part)
        }
    }
    return tokens
}

fun List<InstructionToken>.extractFloatValue(
    floatVariables: Map<String, MutableFloatState>
): State<Float> {
    return derivedStateOf {
        var result = 0f
        var i = 0
        while (i <= lastIndex) {
            val token = this[i]
            when (token) {
                is InstructionToken.FLOAT -> result += token.value
                is InstructionToken.VARIABLE -> result += token.name.getVariableState(floatVariables).floatValue
                is InstructionToken.MATH_OPERATION -> {
                    val following = this[i + 1]
                    val targetValue = when (following) {
                        is InstructionToken.FLOAT -> following.value
                        is InstructionToken.VARIABLE -> following.name.getVariableState(floatVariables).floatValue
                        else -> error("Invalid token after math op")
                    }
                    result = applyMathOperation(result, targetValue, token.type)

                    i++ // Skip next variable, since it already is used here.
                }
                is InstructionToken.PARANTHESIS -> error("Not supported yet!")
            }
            i++
        }
        result
    }
}

private fun applyMathOperation(initialValue: Float, targetValue: Float, operation: MathOperationType): Float {
    return when (operation) {
        MathOperationType.ADDITION -> initialValue + targetValue
        MathOperationType.SUBTRACTION -> initialValue - targetValue
        MathOperationType.MULTIPLICATION -> initialValue * targetValue
        MathOperationType.DIVISION -> initialValue / targetValue
    }
}

sealed interface InstructionToken {
    data class VARIABLE (val name: String) : InstructionToken
    data class FLOAT (val value: Float) : InstructionToken
    data class MATH_OPERATION (val type: MathOperationType) : InstructionToken
    data class PARANTHESIS (val type : ParanthesisType) : InstructionToken
}

enum class MathOperationType {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION;

    companion object {
        fun parse(operation: String) : MathOperationType {
            return when (operation) {
               "+"  -> ADDITION
                "-" -> SUBTRACTION
                "*" -> MULTIPLICATION
                "/" -> DIVISION
                else -> error("Unknown math operation: $operation")
            }
        }
    }
}

enum class ParanthesisType {
    LEFT, RIGHT;

    companion object {
        fun parse(paranthesis: String) : ParanthesisType {
            return when (paranthesis) {
                "(" -> LEFT
                ")" -> RIGHT
                else -> error("Unknown paranthesis: $paranthesis")
            }
        }
    }
}