package farid.guliyev.mblockly.domain.model

import farid.guliyev.mblockly.domain.model.Instruction.Variables.DefineFloat
import farid.guliyev.mblockly.ui.components.ColorValidator
import farid.guliyev.mblockly.ui.components.FloatValidator
import farid.guliyev.mblockly.ui.components.IntValidator
import farid.guliyev.mblockly.ui.components.StringValidator

/** MAKE SURE For each Instruction, there is associated Instruction. Be careful here overall, do not mess it up :D */

sealed interface Instruction {

    fun changeOptionalFieldByName(name: String, isEnabled: Boolean) : Instruction {
        if (this is Visuals.DrawShape) {
            val field = Visuals.DrawShape.OptionalFields.valueOf(name)
            var updatedInstruction = this.updateField(
                enabledOptionalFields = if (isEnabled) enabledOptionalFields + field else enabledOptionalFields - field
            )

            if (!isEnabled) { updatedInstruction = updatedInstruction.resetOptionalField(field ) }
            return updatedInstruction
        }

        else error("Unsupported operation: enableOptionalFieldByName($name)")
    }

    sealed interface Controls : Instruction {
        data class Wait(
            val durationField: InstructionField<Int, Wait> = InstructionField(label = "Duration (ms)", value = "1000", validator = IntValidator, onEdit = { old, value -> old.updateField(duration = value) }),
        ) : Controls {
            private fun updateField(
                duration: String = this.durationField.value
            ) : Wait {
                return Wait(
                    durationField = this.durationField.copy(value = duration),
                )
            }
        }
    }

    sealed interface Variables : Instruction {
        data class DefineString(val name: String = "word", val value: String = "") : Variables
        data class DefineInteger(val name: String = "integer", val value: Int = 0) : Variables
        data class DefineFloat(
            val nameField: InstructionField<String, DefineFloat> = InstructionField(label = "Name", value = "float", validator = StringValidator, onEdit = { old, value -> old.updateField(name = value) }),
            val valueField: InstructionField<Float, DefineFloat> = InstructionField(label = "Value", value = "0.0", validator = FloatValidator, onEdit = { old, value -> old.updateField(value = value) }),
        ) : Variables {
            private fun updateField(
                name: String = this.nameField.value,
                value: String = this.valueField.value
            ) : DefineFloat {
                return DefineFloat(
                    nameField = this.nameField.copy(value = name),
                    valueField = this.valueField.copy(value = value),
                )
            }
        }
    }

    sealed interface Animations : Instruction {
        data class AnimateFloat(
            val nameField: InstructionField<String, AnimateFloat> = InstructionField(label = "Name", value = "float", validator = StringValidator, onEdit = { old, value -> old.updateField(name = value) }),
            val valueField: InstructionField<Float, AnimateFloat> = InstructionField(label = "Value", value = "0.0", validator = FloatValidator, onEdit = { old, value -> old.updateField(value = value) }),
            val durationField: InstructionField<Int, AnimateFloat> = InstructionField(label = "Duration (ms)", value = "1000", validator = IntValidator, onEdit = { old, value -> old.updateField(duration = value) }),
        ) : Animations {
            private fun updateField(
                name: String = this.nameField.value,
                value: String = this.valueField.value,
                duration: String = this.durationField.value,
            ) : AnimateFloat {
                return AnimateFloat(
                    nameField = this.nameField.copy(value = name),
                    valueField = this.valueField.copy(value = value),
                    durationField = this.durationField.copy(value = duration)
                )
            }
        }
    }

    sealed interface Visuals : Instruction {
        data class DrawShape(
            val name: InstructionField<String, DrawShape> = InstructionField(label = "Name", value = "shape", onEdit = { old, value -> old.updateField(name = value) }),
            val x: InstructionField<Float, DrawShape> = InstructionField(label = "X", value = "0.0", onEdit = { old, value -> old.updateField(x = value) }),
            val y: InstructionField<Float, DrawShape> = InstructionField(label = "Y", value = "0.0", onEdit = { old, value -> old.updateField(y = value) }),
            val width: InstructionField<Float, DrawShape> = InstructionField(label = "Width", value = "200.0", onEdit = { old, value -> old.updateField(width = value) }),
            val height: InstructionField<Float, DrawShape> = InstructionField(label = "Height", value = "200.0", onEdit = { old, value -> old.updateField(height = value) }),
            val cornerRadius: InstructionField<Float, DrawShape> = InstructionField(label = "Corner Radius", value = "0.0", onEdit = { old, value -> old.updateField(cornerRadius = value) }),
            val color: InstructionField<Long, DrawShape> = InstructionField(label = "Color", value = "FF0000FF", validator = ColorValidator, onEdit = { old, value -> old.updateField(color = value) }),
            val scaleField: InstructionField<Float, DrawShape> = InstructionField(label = "Scale", value = "1.0", onEdit = { old, value -> old.updateField(scale = value) }),
            // Optional field flags
            val enabledOptionalFields: Set<OptionalFields> = emptySet()
        ) : Visuals {
            enum class OptionalFields {
                CORNER_RADIUS_FIELD, COLOR_FIELD, SCALE_FIELD
            }

            fun updateField(
                name: String = this.name.value,
                width: String = this.width.value,
                x: String = this.x.value,
                y: String = this.y.value,
                height: String = this.height.value,
                color: String = this.color.value,
                cornerRadius: String = this.cornerRadius.value,
                scale: String = this.scaleField.value,
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
                    scaleField = this.scaleField.copy(value = scale),
                    enabledOptionalFields = enabledOptionalFields
                )
            }

            fun resetOptionalField(
                field: OptionalFields
            ) : DrawShape {
                return when(field) {
                    OptionalFields.CORNER_RADIUS_FIELD -> {
                        updateField(cornerRadius = "0")
                    }

                    OptionalFields.COLOR_FIELD -> {
                        updateField(color = "FF0000FF")
                    }

                    OptionalFields.SCALE_FIELD -> {
                        updateField(scale = "1")
                    }
                }
            }
        }
    }
}

val Instruction.type
    get() = when (this) {
        is Instruction.Variables -> {
            when (this) {
                is Instruction.Variables.DefineString -> InstructionType.SET_STRING
                is Instruction.Variables.DefineInteger -> InstructionType.SET_INTEGER
                is DefineFloat -> InstructionType.SET_FLOAT
            }
        }

        is Instruction.Visuals -> {
            when (this) {
                is Instruction.Visuals.DrawShape -> InstructionType.DRAW_SHAPE
            }
        }

        is Instruction.Animations -> {
            when(this) {
                is Instruction.Animations.AnimateFloat -> InstructionType.ANIMATE_FLOAT
            }
        }

        is Instruction.Controls -> {
            when(this) {
                is Instruction.Controls.Wait -> InstructionType.WAIT
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
    SET_STRING(description = "💾 Set/create a variable of STRING type", label = "Set string"),
    SET_INTEGER(description = "💾 Set/create a new variable of INTEGER type", label = "Set integer"),
    SET_FLOAT(description = "💾 Set/create a new variable of FLOAT type", label = "Set float"),
    ANIMATE_FLOAT(description = "🤸‍♀️ Animate FLOAT", label = "Animate float"),
    DRAW_SHAPE(description = "📐 Draw a shape", label = "Draw shape"),
    WAIT(description = "😴 Wait", label = "Wait")
}

fun InstructionType.init(): Instruction {
    return when (this) {
        InstructionType.SET_STRING -> Instruction.Variables.DefineString()
        InstructionType.SET_INTEGER -> Instruction.Variables.DefineInteger()
        InstructionType.SET_FLOAT -> DefineFloat()
        InstructionType.DRAW_SHAPE -> Instruction.Visuals.DrawShape()
        InstructionType.ANIMATE_FLOAT -> Instruction.Animations.AnimateFloat()
        InstructionType.WAIT -> Instruction.Controls.Wait()
    }
}