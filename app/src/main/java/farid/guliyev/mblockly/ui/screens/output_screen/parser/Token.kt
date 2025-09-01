sealed interface Token {
    data class Atom(val value: String) : Token
    data class Op(val value: String) : Token
    data object EOF : Token
}