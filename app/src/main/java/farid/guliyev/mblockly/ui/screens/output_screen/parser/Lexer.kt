package farid.guliyev.mblockly.ui.screens.output_screen.parser

import Token

class Lexer (val tokens: MutableList<Token>) {
    constructor(input: String) : this(tokens = tokenize(input).toMutableList())

    fun next() : Token {
        return tokens.removeFirstOrNull() ?: return Token.EOF
    }

    fun peek() : Token {
        return tokens.firstOrNull() ?: return Token.EOF
    }
}