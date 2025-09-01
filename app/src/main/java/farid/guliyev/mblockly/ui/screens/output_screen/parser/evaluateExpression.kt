package farid.guliyev.mblockly.ui.screens.output_screen.parser

import Expression
import androidx.compose.runtime.MutableFloatState

fun Expression.evaluate(
    floatVariables: Map<String, MutableFloatState>
) : String {
    val result: String = when (this) {
        is Expression.Operation -> {
            when (op) {
                "+" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs + rhs).toString()
                }
                "-" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs - rhs).toString()
                }
                "*" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs * rhs).toString()
                }
                "/" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs / rhs).toString()
                }
                ">" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs > rhs).toString()
                }
                "<" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs < rhs).toString()
                }
                ">=" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs >= rhs).toString()
                }
                "<=" -> {
                    val lhs = expressions[0].evaluateAsFloat(floatVariables)
                    val rhs = expressions[1].evaluateAsFloat(floatVariables)
                    (lhs <= rhs).toString()
                }
                "==" -> {
                    val lhs = expressions[0].evaluate(floatVariables)
                    val rhs = expressions[1].evaluate(floatVariables)
                    (lhs == rhs).toString()
                }
                "||" -> {
                    val lhs = expressions[0].evaluateAsBool(floatVariables)
                    val rhs = expressions[1].evaluateAsBool(floatVariables)
                    (lhs || rhs).toString()
                }
                "&&" -> {
                    val lhs = expressions[0].evaluateAsBool(floatVariables)
                    val rhs = expressions[1].evaluateAsBool(floatVariables)
                    (lhs && rhs).toString()
                }
                else -> error("Unknown operand: $op")
            }
        }
        is Expression.Atom -> {
            return if (floatVariables.contains(value)) floatVariables[value]!!.value.toString() else value
        }
    }
    return result
}

fun Expression.evaluateAsFloat(floatVariables: Map<String, MutableFloatState>) : Float {
    return evaluate(floatVariables).toFloat()
}

fun Expression.evaluateAsBool(floatVariables: Map<String, MutableFloatState>) : Boolean {
    return evaluate(floatVariables).toBooleanStrict()
}