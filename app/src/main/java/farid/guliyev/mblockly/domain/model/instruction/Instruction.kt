package farid.guliyev.mblockly.domain.model.instruction

import farid.guliyev.mblockly.domain.model.instruction.Instruction.Controls.Wait
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Variables.DefineFloat
import farid.guliyev.mblockly.ui.components.ColorValidator
import farid.guliyev.mblockly.ui.components.FloatValidator
import farid.guliyev.mblockly.ui.components.InstructionField
import farid.guliyev.mblockly.ui.components.IntValidator
import farid.guliyev.mblockly.ui.components.StringValidator
import kotlinx.serialization.Serializable

/** MAKE SURE For each Instruction, there is associated Instruction. Be careful here overall, do not mess it up :D */
sealed interface InstructionRuntime {
    val base : Instruction

    companion object {
        fun fromBase(base: Instruction): InstructionRuntime {
            return when (base) {
                is Wait -> Controls.Wait(base)
                is DefineFloat -> Variables.DefineFloat(base)
                is Instruction.Animations.AnimateFloat -> Animations.AnimateFloat(base)
                is Instruction.Visuals.DrawShape -> Visuals.DrawShape(base)
                is Instruction.Variables.DefineInteger -> error("$base")
                is Instruction.Variables.DefineString -> error("$base")
                // add other instruction types here
            }
        }

    }

    // ------------------- CONTROLS -------------------
    sealed interface Controls : InstructionRuntime {
        class Wait(override val base: Instruction.Controls.Wait = Instruction.Controls.Wait()) : Controls {
            val durationField = InstructionField<Int, Wait>(
                data = base.durationField,
                validator = IntValidator,
                onEdit = { Wait(base.updateField(duration = it)) }
            )
        }
    }

    // ------------------- VARIABLES -------------------
    sealed interface Variables : InstructionRuntime {

        class DefineFloat(override val base: Instruction.Variables.DefineFloat = Instruction.Variables.DefineFloat()) : Variables {
            val nameField = InstructionField<String, DefineFloat>(
                data = base.nameField,
                validator = StringValidator,
                onEdit = { value -> DefineFloat(base.updateField(name = value)) }
            )
            val valueField = InstructionField<String, DefineFloat>(
                data = base.valueField,
                validator = FloatValidator,
                onEdit = { value -> DefineFloat(base.updateField(value = value)) }
            )
        }
    }

    // ------------------- ANIMATIONS -------------------
    sealed interface Animations : InstructionRuntime {
        class AnimateFloat(override val base: Instruction.Animations.AnimateFloat = Instruction.Animations.AnimateFloat()) : Animations {
            val nameField = InstructionField<String, AnimateFloat>(
                base.nameField,
                validator = StringValidator,
                onEdit = { v -> AnimateFloat(base.updateField(name = v)) }
            )
            val valueField = InstructionField<Float, AnimateFloat>(
                base.valueField,
                validator = FloatValidator,
                onEdit = { v -> AnimateFloat(base.updateField(value = v)) }
            )
            val durationField = InstructionField<Int, AnimateFloat>(
                base.durationField,
                validator = IntValidator,
                onEdit = { v -> AnimateFloat(base.updateField(duration = v)) }
            )
        }
    }

    // ------------------- VISUALS -------------------
    sealed interface Visuals : InstructionRuntime {
        class DrawShape(override val base: Instruction.Visuals.DrawShape = Instruction.Visuals.DrawShape()) : Visuals {
            val name = InstructionField<String, DrawShape>(
                base.name,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(name = v)) }
            )
            val x = InstructionField<Float, DrawShape>(
                base.x,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(x = v)) }
            )
            val y = InstructionField<Float, DrawShape>(
                base.y,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(y = v)) }
            )
            val width = InstructionField<Float, DrawShape>(
                base.width,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(width = v)) }
            )
            val height = InstructionField<Float, DrawShape>(
                base.height,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(height = v)) }
            )
            val cornerRadius = InstructionField<Float, DrawShape>(
                base.cornerRadius,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(cornerRadius = v)) }
            )
            val color = InstructionField<String, DrawShape>(
                base.color,
                validator = ColorValidator,
                onEdit = { v -> DrawShape(base.updateField(color = v)) }
            )
            val scaleField = InstructionField<Float, DrawShape>(
                base.scaleField,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(scale = v)) }
            )
        }
    }
}


@Serializable
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

    @Serializable
    sealed interface Controls : Instruction {
        @Serializable
        data class Wait(
            val durationField: InstructionFieldData = InstructionFieldData("Duration (ms)", "1000"),
        ) : Controls {
            fun updateField(
                duration: String
            ) = Wait(durationField = this.durationField.copy(value = duration))
        }
    }

    @Serializable
    sealed interface Variables : Instruction {
        @Serializable
        data class DefineString(val name: String = "word", val value: String = "") : Variables

        @Serializable
        data class DefineInteger(val name: String = "integer", val value: Int = 0) : Variables

        @Serializable
        data class DefineFloat(
            val nameField: InstructionFieldData = InstructionFieldData("Name", "float"),
            val valueField: InstructionFieldData = InstructionFieldData("Value", "0.0"),
        ) : Variables {
            fun updateField(
                name: String = this.nameField.value,
                value: String = this.valueField.value
            ): DefineFloat = DefineFloat(nameField = this.nameField.copy(value = name), valueField = this.valueField.copy(value = value))
        }
    }

    @Serializable
    sealed interface Animations : Instruction {
        @Serializable
        data class AnimateFloat(
            val nameField: InstructionFieldData = InstructionFieldData("Name", "float"),
            val valueField: InstructionFieldData = InstructionFieldData("Value", "0.0"),
            val durationField: InstructionFieldData = InstructionFieldData("Duration (ms)", "1000"),
        ) : Animations {
            fun updateField(
                name: String = this.nameField.value,
                value: String = this.valueField.value,
                duration: String = this.durationField.value,
            ): AnimateFloat =
                AnimateFloat(
                    nameField = this.nameField.copy(value = name),
                    valueField = this.valueField.copy(value = value),
                    durationField = this.durationField.copy(value = duration)
                )
        }
    }

    @Serializable
    sealed interface Visuals : Instruction {
        @Serializable
        data class DrawShape(
            val name: InstructionFieldData = InstructionFieldData("Name", "shape"),
            val x: InstructionFieldData = InstructionFieldData("X", "0.0"),
            val y: InstructionFieldData = InstructionFieldData("Y", "0.0"),
            val width: InstructionFieldData = InstructionFieldData("Width", "200.0"),
            val height: InstructionFieldData = InstructionFieldData("Height", "200.0"),
            val cornerRadius: InstructionFieldData = InstructionFieldData("Corner Radius", "0.0"),
            val color: InstructionFieldData = InstructionFieldData("Color", "FF0000FF"),
            val scaleField: InstructionFieldData = InstructionFieldData("Scale", "1.0"),
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
            ): DrawShape = DrawShape(
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

            fun resetOptionalField(field: OptionalFields): DrawShape =
                when (field) {
                    OptionalFields.CORNER_RADIUS_FIELD -> updateField(cornerRadius = "0")
                    OptionalFields.COLOR_FIELD -> updateField(color = "FF0000FF")
                    OptionalFields.SCALE_FIELD -> updateField(scale = "1")
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
                is Wait -> InstructionType.WAIT
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

fun InstructionType.init(): InstructionRuntime {
    return when (this) {
        InstructionType.SET_STRING -> error("$this")
        InstructionType.SET_INTEGER -> error("$this")
        InstructionType.SET_FLOAT -> InstructionRuntime.Variables.DefineFloat()
        InstructionType.DRAW_SHAPE -> InstructionRuntime.Visuals.DrawShape()
        InstructionType.ANIMATE_FLOAT -> InstructionRuntime.Animations.AnimateFloat()
        InstructionType.WAIT -> InstructionRuntime.Controls.Wait()
    }
}