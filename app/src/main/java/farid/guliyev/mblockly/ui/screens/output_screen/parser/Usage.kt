package farid.guliyev.mblockly.ui.screens.output_screen.parser

fun main() {
    val input = "Health \$health" //"value1 + var2 * var2 - 123 / var3"
    val lexer = Lexer(input)
    println(lexer.tokens)
    val expression = lexer.parseExpression()
    println(expression)
    val result = expression.evaluate(emptyMap())
    println(result)
}