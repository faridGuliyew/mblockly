package farid.guliyev.mblockly.domain.model.instruction

import farid.guliyev.mblockly.domain.model.instruction.Instruction.Controls.Wait
import farid.guliyev.mblockly.domain.model.instruction.Instruction.Variables.DefineFloat
import farid.guliyev.mblockly.ui.components.ColorValidator
import farid.guliyev.mblockly.ui.components.FloatValidator
import farid.guliyev.mblockly.ui.components.IntValidator
import farid.guliyev.mblockly.ui.components.StringValidator
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlock
import kotlinx.serialization.Serializable
import java.util.UUID

/** MAKE SURE For each Instruction, there is associated Instruction. Be careful here overall, do not mess it up :D */
sealed interface InstructionRuntime {
    val base : Instruction

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

        class ChangeFloat(
            override val base: Instruction.Variables.ChangeFloat = Instruction.Variables.ChangeFloat()
        ) : Variables {
            val nameField = InstructionField<String, ChangeFloat>(
                data = base.nameField,
                validator = StringValidator,
                onEdit = { v -> ChangeFloat(base.updateField(name = v)) }
            )
            val deltaField = InstructionField<String, ChangeFloat>(
                data = base.deltaField,
                validator = FloatValidator,
                onEdit = { v -> ChangeFloat(base.updateField(delta = v)) }
            )
            val durationField = InstructionField<String, ChangeFloat>(
                data = base.durationField,
                validator = IntValidator,
                onEdit = { v -> ChangeFloat(base.updateField(duration = v)) }
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
            val rotation = InstructionField<Float, DrawShape>(
                base.rotation,
                validator = StringValidator,
                onEdit = { v -> DrawShape(base.updateField(rotation = v)) }
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

        class DrawLine(override val base: Instruction.Visuals.DrawLine = Instruction.Visuals.DrawLine()) : Visuals {
            val startX = InstructionField<Float, DrawLine>(
                base.startX,
                validator = StringValidator,
                onEdit = { v -> DrawLine(base.updateField(startX = v)) }
            )
            val startY = InstructionField<Float, DrawLine>(
                base.startY,
                validator = StringValidator,
                onEdit = { v -> DrawLine(base.updateField(startY = v)) }
            )
            val endX = InstructionField<Float, DrawLine>(
                base.endX,
                validator = StringValidator,
                onEdit = { v -> DrawLine(base.updateField(endX = v)) }
            )
            val endY = InstructionField<Float, DrawLine>(
                base.endY,
                validator = StringValidator,
                onEdit = { v -> DrawLine(base.updateField(endY = v)) }
            )
            val color = InstructionField<String, DrawLine>(
                base.color,
                validator = ColorValidator,
                onEdit = { v -> DrawLine(base.updateField(color = v)) }
            )
            val thickness = InstructionField<Float, DrawLine>(
                base.thickness,
                validator = StringValidator,
                onEdit = { v -> DrawLine(base.updateField(thickness = v)) }
            )
        }

        class DrawText(override val base: Instruction.Visuals.DrawText = Instruction.Visuals.DrawText()) : Visuals {
            val text = InstructionField<String, DrawText>(
                base.text,
                validator = StringValidator,
                onEdit = { v -> DrawText(base.updateField(text = v)) }
            )
            val x = InstructionField<Float, DrawText>(
                base.x,
                validator = StringValidator,
                onEdit = { v -> DrawText(base.updateField(x = v)) }
            )
            val y = InstructionField<Float, DrawText>(
                base.y,
                validator = StringValidator,
                onEdit = { v -> DrawText(base.updateField(y = v)) }
            )
            val color = InstructionField<String, DrawText>(
                base.color,
                validator = ColorValidator,
                onEdit = { v -> DrawText(base.updateField(color = v)) }
            )
            val fontSize = InstructionField<Float, DrawText>(
                base.fontSize,
                validator = StringValidator,
                onEdit = { v -> DrawText(base.updateField(fontSize = v)) }
            )
        }

        class DrawImage(override val base: Instruction.Visuals.DrawImage = Instruction.Visuals.DrawImage()) : Visuals {
            val name = InstructionField<String, DrawImage>(
                base.name,
                validator = StringValidator,
                onEdit = { v -> DrawImage(base.updateField(name = v)) }
            )
            val imageName = InstructionField<String, DrawImage>(
                base.imageName,
                validator = StringValidator,
                onEdit = { v -> DrawImage(base.updateField(imageName = v)) }
            )
            val x = InstructionField<Float, DrawImage>(
                base.x,
                validator = StringValidator,
                onEdit = { v -> DrawImage(base.updateField(x = v)) }
            )
            val y = InstructionField<Float, DrawImage>(
                base.y,
                validator = StringValidator,
                onEdit = { v -> DrawImage(base.updateField(y = v)) }
            )
            val width = InstructionField<Float, DrawImage>(
                base.width,
                validator = StringValidator,
                onEdit = { v -> DrawImage(base.updateField(width = v)) }
            )
            val height = InstructionField<Float, DrawImage>(
                base.height,
                validator = StringValidator,
                onEdit = { v -> DrawImage(base.updateField(height = v)) }
            )
        }
    }

    // ------------------- MEDIA -------------------
    sealed interface Media : InstructionRuntime {
        class PlaySound(
            override val base: Instruction.Media.PlaySound = Instruction.Media.PlaySound()
        ) : Media {
            val fileName = InstructionField<String, PlaySound>(
                base.fileName,
                validator = StringValidator,
                onEdit = { v -> PlaySound(base.updateField(fileName = v)) }
            )
            val repeatCount = InstructionField<Float, PlaySound>(
                base.repeatCount,
                validator = StringValidator,
                onEdit = { v -> PlaySound(base.updateField(repeatCount = v)) }
            )
        }
    }
}


@Serializable
sealed interface Instruction {

    fun buildRuntime() : InstructionRuntime

    fun changeOptionalFieldByName(name: String, isEnabled: Boolean) : Instruction {
        when (this) {
            is Visuals.DrawShape -> {
                val field = Visuals.DrawShape.OptionalFields.valueOf(name)
                var updatedInstruction = this.updateField(
                    enabledOptionalFields = if (isEnabled) enabledOptionalFields + field else enabledOptionalFields - field
                )

                if (!isEnabled) { updatedInstruction = updatedInstruction.resetOptionalField(field ) }
                return updatedInstruction
            }
            is Visuals.DrawLine -> {
                val field = Visuals.DrawLine.OptionalFields.valueOf(name)
                var updatedInstruction = this.updateField(
                    enabledOptionalFields = if (isEnabled) enabledOptionalFields + field else enabledOptionalFields - field
                )

                if (!isEnabled) { updatedInstruction = updatedInstruction.resetOptionalField(field ) }
                return updatedInstruction
            }
            is Variables.ChangeFloat -> {
                val field = Variables.ChangeFloat.OptionalFields.valueOf(name)
                var updatedInstruction = this.updateField(
                    enabledOptionalFields = if (isEnabled) enabledOptionalFields + field else enabledOptionalFields - field
                )

                if (!isEnabled) { updatedInstruction = updatedInstruction.resetOptionalField(field ) }
                return updatedInstruction
            }
            else -> error("Unsupported operation: enableOptionalFieldByName($name)")
        }
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

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Controls.Wait(this)
            }
        }
    }

    @Serializable
    sealed interface Variables : Instruction {
        @Serializable
        data class DefineString(val name: String = "word", val value: String = "") : Variables {
            override fun buildRuntime(): InstructionRuntime {
                error("buildRuntime: $this")
            }
        }

        @Serializable
        data class DefineFloat(
            val nameField: InstructionFieldData = InstructionFieldData("Name", "float"),
            val valueField: InstructionFieldData = InstructionFieldData("Value", "0.0"),
        ) : Variables {
            fun updateField(
                name: String = this.nameField.value,
                value: String = this.valueField.value
            ): DefineFloat = DefineFloat(nameField = this.nameField.copy(value = name), valueField = this.valueField.copy(value = value))

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Variables.DefineFloat(this)
            }
        }

        @Serializable
        data class ChangeFloat(
            val nameField: InstructionFieldData = InstructionFieldData("Name", "float"),
            val deltaField: InstructionFieldData = InstructionFieldData("Change By", "1.0"),
            val durationField: InstructionFieldData = InstructionFieldData("Duration (ms)", "0"),
            val enabledOptionalFields: Set<OptionalFields> = emptySet()
        ) : Variables {
            enum class OptionalFields {
                DURATION_FIELD
            }

            fun updateField(
                name: String = this.nameField.value,
                delta: String = this.deltaField.value,
                duration: String = this.durationField.value,
                enabledOptionalFields: Set<OptionalFields> = this.enabledOptionalFields
            ) = ChangeFloat(
                nameField = this.nameField.copy(value = name),
                deltaField = this.deltaField.copy(value = delta),
                durationField = this.durationField.copy(value = duration),
                enabledOptionalFields = enabledOptionalFields
            )

            fun resetOptionalField(field: OptionalFields): ChangeFloat =
                when (field) {
                    OptionalFields.DURATION_FIELD -> updateField(duration = "0")
                }

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Variables.ChangeFloat(this)
            }
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

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Animations.AnimateFloat(this)
            }
        }
    }

    @Serializable
    sealed interface Visuals : Instruction {
        @Serializable
        data class DrawShape(
            val name: InstructionFieldData = InstructionFieldData("Name", UUID.randomUUID().toString().take(10)),
            val x: InstructionFieldData = InstructionFieldData("X", "0.0"),
            val y: InstructionFieldData = InstructionFieldData("Y", "0.0"),
            val width: InstructionFieldData = InstructionFieldData("Width", "200.0"),
            val height: InstructionFieldData = InstructionFieldData("Height", "200.0"),
            val cornerRadius: InstructionFieldData = InstructionFieldData("Corner Radius", "0.0"),
            val rotation: InstructionFieldData = InstructionFieldData("Rotation", "0.0"),
            val color: InstructionFieldData = InstructionFieldData("Color", "FF0000FF"),
            val scaleField: InstructionFieldData = InstructionFieldData("Scale", "1.0"),
            val enabledOptionalFields: Set<OptionalFields> = emptySet()
        ) : Visuals {

            enum class OptionalFields {
                NAME_FIELD, CORNER_RADIUS_FIELD, COLOR_FIELD, SCALE_FIELD, ROTATION
            }

            fun updateField(
                name: String = this.name.value,
                width: String = this.width.value,
                x: String = this.x.value,
                y: String = this.y.value,
                height: String = this.height.value,
                color: String = this.color.value,
                cornerRadius: String = this.cornerRadius.value,
                rotation: String = this.rotation.value,
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
                rotation = this.rotation.copy(value = rotation),
                scaleField = this.scaleField.copy(value = scale),
                enabledOptionalFields = enabledOptionalFields
            )

            fun resetOptionalField(field: OptionalFields): DrawShape =
                when (field) {
                    OptionalFields.CORNER_RADIUS_FIELD -> updateField(cornerRadius = "0")
                    OptionalFields.COLOR_FIELD -> updateField(color = "FF0000FF")
                    OptionalFields.SCALE_FIELD -> updateField(scale = "1")
                    OptionalFields.ROTATION -> updateField(rotation = "0.0")
                    OptionalFields.NAME_FIELD -> updateField(name = UUID.randomUUID().toString().take(10))
                }

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Visuals.DrawShape(this)
            }
        }


        @Serializable
        data class DrawLine(
            val startX: InstructionFieldData = InstructionFieldData("Start X", "0.0"),
            val startY: InstructionFieldData = InstructionFieldData("Start Y", "0.0"),
            val endX: InstructionFieldData = InstructionFieldData("End X", "100.0"),
            val endY: InstructionFieldData = InstructionFieldData("End Y", "100.0"),
            val color: InstructionFieldData = InstructionFieldData("Color", "FF0000FF"),
            val thickness: InstructionFieldData = InstructionFieldData("Thickness", "1.0"),
            val enabledOptionalFields: Set<OptionalFields> = emptySet()
        ) : Visuals {

            enum class OptionalFields {
                COLOR_FIELD, THICKNESS_FIELD
            }

            fun updateField(
                startX: String = this.startX.value,
                startY: String = this.startY.value,
                endX: String = this.endX.value,
                endY: String = this.endY.value,
                color: String = this.color.value,
                thickness: String = this.thickness.value,
                enabledOptionalFields: Set<OptionalFields> = this.enabledOptionalFields
            ): DrawLine = DrawLine(
                startX = this.startX.copy(value = startX),
                startY = this.startY.copy(value = startY),
                endX = this.endX.copy(value = endX),
                endY = this.endY.copy(value = endY),
                color = this.color.copy(value = color),
                thickness = this.thickness.copy(value = thickness),
                enabledOptionalFields = enabledOptionalFields
            )

            fun resetOptionalField(field: OptionalFields): DrawLine =
                when (field) {
                    OptionalFields.COLOR_FIELD -> updateField(color = "FF0000FF")
                    OptionalFields.THICKNESS_FIELD -> updateField(thickness = "1.0")
                }

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Visuals.DrawLine(this)
            }
        }

        @Serializable
        data class DrawText(
            val text: InstructionFieldData = InstructionFieldData("Text", "Hello MBlockly!"),
            val x: InstructionFieldData = InstructionFieldData("X", "0.0"),
            val y: InstructionFieldData = InstructionFieldData("Y", "0.0"),
            val color: InstructionFieldData = InstructionFieldData("Color", "FF000000"),
            val fontSize: InstructionFieldData = InstructionFieldData("Font Size", "50.0"),
        ) : Visuals {

            fun updateField(
                text: String = this.text.value,
                x: String = this.x.value,
                y: String = this.y.value,
                color: String = this.color.value,
                fontSize: String = this.fontSize.value
            ): DrawText = DrawText(
                text = this.text.copy(value = text),
                x = this.x.copy(value = x),
                y = this.y.copy(value = y),
                color = this.color.copy(value = color),
                fontSize = this.fontSize.copy(value = fontSize)
            )

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Visuals.DrawText(this)
            }
        }

        @Serializable
        data class DrawImage(
            val name: InstructionFieldData = InstructionFieldData("Name", UUID.randomUUID().toString().take(10)),
            val imageName: InstructionFieldData = InstructionFieldData("Image Name", "image.jpg"),
            val x: InstructionFieldData = InstructionFieldData("X", "0.0"),
            val y: InstructionFieldData = InstructionFieldData("Y", "0.0"),
            val width: InstructionFieldData = InstructionFieldData("Width", "200.0"),
            val height: InstructionFieldData = InstructionFieldData("Height", "200.0")
        ) : Visuals {

            fun updateField(
                name: String = this.name.value,
                imageName: String = this.imageName.value,
                x: String = this.x.value,
                y: String = this.y.value,
                width: String = this.width.value,
                height: String = this.height.value
            ): DrawImage = DrawImage(
                name = this.name.copy(value = name),
                imageName = this.imageName.copy(value = imageName),
                x = this.x.copy(value = x),
                y = this.y.copy(value = y),
                width = this.width.copy(value = width),
                height = this.height.copy(value = height)
            )

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Visuals.DrawImage(this)
            }
        }
    }

    @Serializable
    sealed interface Media : Instruction {
        @Serializable
        data class PlaySound(
            val fileName: InstructionFieldData = InstructionFieldData("File Name", "sound.mp3"),
            val repeatCount: InstructionFieldData = InstructionFieldData("Repeat Count", "1.0")
        ) : Media {

            fun updateField(
                fileName: String = this.fileName.value,
                repeatCount: String = this.repeatCount.value
            ): PlaySound = PlaySound(
                fileName = this.fileName.copy(value = fileName),
                repeatCount = this.repeatCount.copy(value = repeatCount)
            )

            override fun buildRuntime(): InstructionRuntime {
                return InstructionRuntime.Media.PlaySound(this)
            }
        }
    }
}

val Instruction.type
    get() = when (this) {
        is Instruction.Variables -> {
            when (this) {
                is Instruction.Variables.DefineString -> SingleInstructionType.SET_STRING
                is DefineFloat -> SingleInstructionType.SET_FLOAT
                is Instruction.Variables.ChangeFloat -> SingleInstructionType.CHANGE_FLOAT
            }
        }

        is Instruction.Visuals -> {
            when (this) {
                is Instruction.Visuals.DrawShape -> SingleInstructionType.DRAW_SHAPE
                is Instruction.Visuals.DrawLine -> SingleInstructionType.DRAW_LINE
                is Instruction.Visuals.DrawText -> SingleInstructionType.DRAW_TEXT
                is Instruction.Visuals.DrawImage -> SingleInstructionType.DRAW_IMAGE
            }
        }

        is Instruction.Animations -> {
            when(this) {
                is Instruction.Animations.AnimateFloat -> SingleInstructionType.ANIMATE_FLOAT
            }
        }

        is Instruction.Controls -> {
            when(this) {
                is Wait -> SingleInstructionType.WAIT
            }
        }
        is Instruction.Media -> {
            when (this) {
                is Instruction.Media.PlaySound -> SingleInstructionType.PLAY_SOUND
            }
        }
    }

val Instruction.optionalFields: List<String>
    get() = when (this) {
        is Instruction.Visuals -> {
            when (this) {
                is Instruction.Visuals.DrawShape -> Instruction.Visuals.DrawShape.OptionalFields.entries.map { it.name }
                is Instruction.Visuals.DrawLine -> Instruction.Visuals.DrawLine.OptionalFields.entries.map { it.name }
                is Instruction.Visuals.DrawText -> emptyList()
                is Instruction.Visuals.DrawImage -> emptyList()
            }
        }
        is Instruction.Variables -> when (this) {
            is Instruction.Variables.ChangeFloat -> Instruction.Variables.ChangeFloat.OptionalFields.entries.map { it.name }
            else -> emptyList()
        }
        else -> emptyList()
    }


enum class SingleInstructionType(val description: String, val label: String = "") {
    SET_STRING(description = "💾 Set/create a variable of STRING type", label = "Set string"),
    SET_FLOAT(description = "💾 Set/create a new variable of FLOAT type", label = "Set float"),
    CHANGE_FLOAT("➕ Change FLOAT by value", "Change float"),
    ANIMATE_FLOAT(description = "🤸‍♀️ Animate FLOAT", label = "Animate float"),
    DRAW_SHAPE(description = "📐 Draw a shape", label = "Draw shape"),
    DRAW_LINE("📏 Draw a line", "Draw line"),
    DRAW_TEXT(description = "🔤 Draw text", label = "Draw text"),
    DRAW_IMAGE(description = "🖼️ Draw an image", label = "Draw image"),
    WAIT(description = "😴 Wait", label = "Wait"),
    PLAY_SOUND(description = "🔊 Play a sound", label = "Play sound"),
}

fun SingleInstructionType.init(): InstructionRuntime {
    return when (this) {
        SingleInstructionType.SET_STRING -> error("$this")
        SingleInstructionType.SET_FLOAT -> InstructionRuntime.Variables.DefineFloat()
        SingleInstructionType.CHANGE_FLOAT -> InstructionRuntime.Variables.ChangeFloat()
        SingleInstructionType.DRAW_SHAPE -> InstructionRuntime.Visuals.DrawShape()
        SingleInstructionType.DRAW_LINE -> InstructionRuntime.Visuals.DrawLine()
        SingleInstructionType.DRAW_TEXT -> InstructionRuntime.Visuals.DrawText()
        SingleInstructionType.DRAW_IMAGE -> InstructionRuntime.Visuals.DrawImage()
        SingleInstructionType.ANIMATE_FLOAT -> InstructionRuntime.Animations.AnimateFloat()
        SingleInstructionType.WAIT -> InstructionRuntime.Controls.Wait()
        SingleInstructionType.PLAY_SOUND -> InstructionRuntime.Media.PlaySound()
    }
}