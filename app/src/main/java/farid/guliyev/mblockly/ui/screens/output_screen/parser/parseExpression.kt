package farid.guliyev.mblockly.ui.screens.output_screen.parser

import Expression

fun Lexer.parseExpression(bp: Float = 0F) : Expression {
    var lhs : Expression = when(val token = next()) {
        is Token.Atom -> Expression.Atom(token.value)
        is Token.Op -> {
            if (token.value == "(") {
                parseExpression(0F).also {
                    val endsWithParanthesis = next() == Token.Op(")")
                    if (!endsWithParanthesis) error("Expected ). Found EOF!")
                }
            } else {
                error("Bad token: $token")
            }
        }
        else -> error("EOF!")
    }
    while (true) {
        val operator = when (val token = peek()) {
            Token.EOF -> break
            is Token.Op -> token
            is Token.Atom -> error("Bad token: $token")
        }

        if (operator.value == ")") break

        val (leftBp, rightBp) = operator.getBindingPower()
        if (leftBp < bp) break
        next()
        val rhs : Expression = parseExpression(rightBp)
        lhs = Expression.Operation(operator.value, listOf(lhs, rhs))
    }

    return lhs
}