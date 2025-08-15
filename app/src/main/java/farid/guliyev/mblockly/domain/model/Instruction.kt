package farid.guliyev.mblockly.domain.model

import farid.guliyev.mblockly.ui.components.ColorValidator
import farid.guliyev.mblockly.ui.components.FloatValidator
import farid.guliyev.mblockly.ui.components.StringValidator

/** MAKE SURE For each Instruction, there is associated Instruction. Be careful here overall, do not mess it up :D */

sealed interface Instruction {

    fun enableOptionalFieldByName(name: String) : Instruction {
        if (this is Visuals.DrawShape) {
            return this.updateField(enabledOptionalFields = enabledOptionalFields + Visuals.DrawShape.OptionalFields.valueOf(name))
        }

        else error("Unsupported operation: enableOptionalFieldByName($name)")
    }

    sealed interface Variables : Instruction {
        data class DefineWord(val name: String = "word", val value: String = "") : Variables
        data class DefineInteger(val name: String = "integer", val value: Int = 0) : Variables
        data class DefineDouble(val name: String = "float", val value: Double = 0.0) : Variables
    }


    @OptIn(ExperimentalStdlibApi::class)
    sealed interface Visuals : Instruction {
        data class DrawShape(
            val name: InstructionField<String, DrawShape> = InstructionField(label = "Name", value = "shape", validator = StringValidator, onEdit = { old, value -> old.updateField(name = value) }),
            val x: InstructionField<Float, DrawShape> = InstructionField(label = "X", value = 0.0F, onEdit = { old, value -> old.updateField(x = value.toFloat()) }),
            val y: InstructionField<Float, DrawShape> = InstructionField(label = "Y", value = 0.0F, onEdit = { old, value -> old.updateField(y = value.toFloat()) }),
            val width: InstructionField<Float, DrawShape> = InstructionField(label = "Width", value = 200.0F, onEdit = { old, value -> old.updateField(width = value.toFloat()) }),
            val height: InstructionField<Float, DrawShape> = InstructionField(label = "Height", value = 200.0F, onEdit = { old, value -> old.updateField(height = value.toFloat()) }),
            val color: InstructionField<Long, DrawShape> = InstructionField(label = "Color", value = 0xFF0000FF, validator = ColorValidator, printFormat = { it.toHexString() }, onEdit = { old, value -> old.updateField(color = value.toLong(16)) }),
            val cornerRadius: InstructionField<Float, DrawShape> = InstructionField(label = "Corner Radius", value = 0F, validator = FloatValidator, onEdit = { old, value -> old.updateField(cornerRadius = value.toFloat()) }),
            // Optional field flags
            val enabledOptionalFields: Set<OptionalFields> = emptySet()
        ) : Visuals {
            enum class OptionalFields {
                CORNER_RADIUS_FIELD, COLOR_FIELD
            }

            fun updateField(
                name: String = this.name.value,
                width: Float = this.width.value,
                x: Float = this.x.value,
                y: Float = this.y.value,
                height: Float = this.height.value,
                color: Long = this.color.value,
                cornerRadius: Float = this.cornerRadius.value,
                enabledOptionalFields: Set<OptionalFields> = this.enabledOptionalFields
            ): DrawShape {
                return DrawShape(
                    name = this.name.copy(value = name),
                    x = this.x.copy(value = x),
                    y = this.y.copy(value = y),
                    width = this.width.copy(value = width),
                    height = this.height.copy(value = height),
                    color = this.color.copy(value = color),
                    cornerRadius = this.cornerRadius.copy(value = cornerRadius),
                    enabledOptionalFields = enabledOptionalFields
                )
            }
        }
    }
}

val Instruction.type
    get() = when (this) {
        is Instruction.Variables -> {
            when (this) {
                is Instruction.Variables.DefineWord -> InstructionType.DEFINE_WORD
                is Instruction.Variables.DefineInteger -> InstructionType.DEFINE_INTEGER
                is Instruction.Variables.DefineDouble -> InstructionType.DEFINE_DOUBLE
            }
        }

        is Instruction.Visuals -> {
            when (this) {
                is Instruction.Visuals.DrawShape -> InstructionType.DRAW_SHAPE
            }
        }
    }

val Instruction.optionalFields: List<String>
    get() = when (this) {
        is Instruction.Visuals -> {
            when (this) {
                is Instruction.Visuals.DrawShape -> Instruction.Visuals.DrawShape.OptionalFields.entries.map { it.name }
            }
        }
        else -> emptyList()
    }


enum class InstructionType(val description: String, val label: String = "") {
    DEFINE_WORD(description = "Define a new variable of string type"),
    DEFINE_INTEGER(description = "Define a new variable of integer type"),
    DEFINE_DOUBLE(description = "Define a new variable of float type"),
    DRAW_SHAPE(description = "Draw a shape", label = "Draw shape")
}

fun InstructionType.init(): Instruction {
    return when (this) {
        InstructionType.DEFINE_WORD -> Instruction.Variables.DefineWord()
        InstructionType.DEFINE_INTEGER -> Instruction.Variables.DefineInteger()
        InstructionType.DEFINE_DOUBLE -> Instruction.Variables.DefineDouble()
        InstructionType.DRAW_SHAPE -> Instruction.Visuals.DrawShape()
    }
}