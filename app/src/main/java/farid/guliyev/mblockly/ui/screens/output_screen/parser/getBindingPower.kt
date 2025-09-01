package farid.guliyev.mblockly.ui.screens.output_screen.parser

fun Token.Op.getBindingPower() : Pair<Float, Float> {
    return when (value) {
        in setOf("+", "-") -> 1.0F to 1.1F
        in setOf("*", "/") -> 2.0F to 2.1F
        in setOf("(", ")") -> 3.0F to 3.1F
        in setOf(">", "<", ">=", "<=", "==") -> 0.0F to 0.1F
        in setOf("||", "&&") -> 0.0F to 0.1F
        else -> error("Unknown operand $value")
    }
}