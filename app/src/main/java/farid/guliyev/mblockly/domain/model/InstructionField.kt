package farid.guliyev.mblockly.domain.model

import farid.guliyev.mblockly.ui.components.FloatValidator
import farid.guliyev.mblockly.ui.components.StringValidator

interface InstructionField <T, P> {
    val label: String
    val value: String
    val validator: (String) -> Boolean
    val onEdit : (old: P, value: String) -> P

    fun copy(
        label: String = this.label,
        value: String = this.value,
    ) : InstructionField<T, P> {
        return InstructionField(label = label, value = value, validator = validator, onEdit = onEdit)
    }

    override fun toString() : String
}

fun <T, P> InstructionField(
    label: String,
    value: String,
    validator: (String) -> Boolean = StringValidator,
    onEdit: (old: P,value: String) -> P,
    printer: (String) -> String = { it }
) = object : InstructionField<T, P> {
    override val label: String = label
    override val value: String = value
    override val validator: (String) -> Boolean = validator
    override val onEdit: (old: P,value: String) -> P = onEdit
    override fun toString(): String { return printer(value) }
}