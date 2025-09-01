sealed interface Expression {
    data class Atom(val value: String) : Expression
    data class Operation(val op: String, val expressions: List<Expression>) : Expression
}