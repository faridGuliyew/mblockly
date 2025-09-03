package farid.guliyev.mblockly.ui.screens.output_screen.parser

import Token

fun tokenize(input: String) : List<Token> {
    val words = input.split(" ").filterNot { it.isEmpty() }

    return buildList {
        words.forEach { word->
            val regex = """\d+\.\d+|\d+|[a-zA-Z_]\w*|<=|>=|==|\|\||&&|\W""".toRegex()
            val parts = regex.findAll(word).map { it.value }
            parts.forEach { part->
                val isAtom = part.all { it.isLetterOrDigit() } || part.toFloatOrNull() != null
                val token = if (isAtom) Token.Atom(part) else Token.Op(part)
                add(token)
            }
        }
        add(Token.EOF)
    }
}