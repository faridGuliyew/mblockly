package farid.guliyev.mblockly.domain.model.instruction

import farid.guliyev.mblockly.ui.components.StringValidator
import kotlinx.serialization.Serializable

@Serializable
data class InstructionFieldData(
    val label: String,
    val value: String
)

class InstructionField<T, P>(
    val data: InstructionFieldData,
    val validator: (String) -> Boolean,
    val onEdit: (value: String) -> P,
    val printer: (String) -> String = { it }
) {
    val label: String get() = data.label
    val value: String get() = data.value

    fun copy(
        label: String = this.label,
        value: String = this.value,
    ): InstructionField<T, P> = InstructionField(
        InstructionFieldData(label, value),
        validator,
        onEdit
    )

    override fun toString(): String = printer(data.value)
}