package farid.guliyev.mblockly.domain.model

import farid.guliyev.mblockly.ui.components.FloatValidator

interface InstructionField <T, P> {
    val label: String
    val value: T
    val validator: (String) -> Boolean
    val isRequired: Boolean get() = true
    val onEdit : (old: P, value: String) -> P

    fun copy(
        label: String = this.label,
        value: T = this.value,
    ) : InstructionField<T, P> {
        return InstructionField(label = label, value = value, validator = validator, onEdit = onEdit)
    }

    override fun toString() : String
}

fun <T, P> InstructionField(
    label: String,
    value: T,
    validator: (String) -> Boolean = FloatValidator,
    onEdit: (old: P,value: String) -> P,
    printFormat: (T) -> String = { it.toString() }
) = object : InstructionField<T, P> {
    override val label: String = label
    override val value: T = value
    override val validator: (String) -> Boolean = validator
    override val onEdit: (old: P,value: String) -> P = onEdit
    override fun toString(): String { return printFormat(value) }
}