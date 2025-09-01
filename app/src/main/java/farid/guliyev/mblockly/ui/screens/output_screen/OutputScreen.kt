package farid.guliyev.mblockly.ui.screens.output_screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.UNDEFINED_VARIABLE
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.ui.model.UiLine
import farid.guliyev.mblockly.ui.model.UiShape
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionGroupMetaData
import farid.guliyev.mblockly.ui.screens.output_screen.components.OutputTopBar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ColumnScope.OutputScreen(
    mainInstructionGroup: InstructionBlock.InstructionGroup,
    onFinish: () -> Unit,
    currentDrag: Float = 0.5F,
    onError: (Exception) -> Unit,
    onDrag: (amount: Float) -> Unit
) {
    val shapes = remember { mutableStateMapOf<String, UiShape>() }
    val lines = remember { mutableStateMapOf<String, UiLine>() }
    val floatVariableStates = remember { mutableStateMapOf<String, MutableFloatState>() }
    LaunchedEffect(Unit) {
        try {
            handleInstructionGroup(
                group = mainInstructionGroup,
                shapes = shapes,
                floatVariableStates = floatVariableStates,
                lines = lines,
                onError = onError
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onError(e)
        }
    }

    Scaffold(
        modifier = Modifier
            .weight(currentDrag)
            .fillMaxSize(),
        topBar = {
            val currentDrag by rememberUpdatedState(currentDrag)
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            OutputTopBar(
                modifier = Modifier.pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        val newDrag = (currentDrag - dragAmount.y / (screenHeight.toPx())).coerceIn(
                            0.1F,
                            0.99F
                        )
                        println("newDrag: $newDrag")
                        onDrag(newDrag)
                    }
                },
                onFinish = onFinish
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            // DRAW SHAPES
            shapes.values.forEach { shape ->
                Canvas(modifier = Modifier) {
                    withTransform(transformBlock = {
                        scale(scale = shape.scale.value)
                        translate(left = shape.x.value, top = shape.y.value)
                        rotate(
                            degrees = shape.rotation.value,
                            pivot = Offset(shape.width.value / 2F, shape.height.value / 2F)
                        )
                    }) {
                        drawRoundRect(
                            color = Color(shape.color),
                            size = Size(shape.width.value, shape.height.value),
                            cornerRadius = CornerRadius(
                                x = shape.cornerRadius.value,
                                y = shape.cornerRadius.value
                            )
                        )
                    }
                }
            }

            // DRAW LINES
            lines.values.forEach { line ->
                Canvas(modifier = Modifier) {
                    drawLine(
                        color = Color(line.color),
                        start = Offset(line.startX.value, line.startY.value),
                        end = Offset(line.endX.value, line.endY.value),
                        strokeWidth = line.thickness.value
                    )
                }
            }
        }
    }
}

suspend fun handleInstructionGroup(
    group: InstructionBlock.InstructionGroup,
    shapes: SnapshotStateMap<String, UiShape>,
    lines: SnapshotStateMap<String, UiLine>,
    floatVariableStates: SnapshotStateMap<String, MutableFloatState>,
    onError: (Exception) -> Unit
) {
    coroutineScope {
        try {
            group.instructionBlocks.forEach { block ->
                when (block) {
                    is InstructionBlock.SingleInstruction -> {
                        handleSingleInstruction(
                            instruction = block.instruction.base,
                            shapes = shapes,
                            lines = lines,
                            floatVariableStates = floatVariableStates,
                            onError = onError
                        )
                        return@forEach
                    }

                    is InstructionBlock.InstructionGroup -> {
                        when (val metadata = block.metaData) {
                            InstructionGroupMetaData.Thread -> {
                                launch {
                                    handleInstructionGroup(
                                        group = block,
                                        shapes = shapes,
                                        floatVariableStates = floatVariableStates,
                                        lines = lines,
                                        onError = onError
                                    )

                                    println("--------------------------------------")
                                    println("OUTPUT OF INSTRUCTION GROUP: ${block.id}")
                                    println("OUTPUT: Shapes: ${shapes.map { it.value }}")
                                    println("OUTPUT: Floats: ${floatVariableStates.keys}")
                                    println("--------------------------------------")
                                }
                            }

                            is InstructionGroupMetaData.FiniteLoop -> {
                                repeat(metadata.loopCount.toInt()) {
                                    handleInstructionGroup(
                                        group = block,
                                        shapes = shapes,
                                        floatVariableStates = floatVariableStates,
                                        lines = lines,
                                        onError = onError
                                    )

                                    println("--------------------------------------")
                                    println("OUTPUT OF INSTRUCTION GROUP: ${block.id}")
                                    println("OUTPUT: Shapes: ${shapes.map { it.value }}")
                                    println("OUTPUT: Floats: ${floatVariableStates.keys}")
                                    println("--------------------------------------")
                                }
                            }

                            InstructionGroupMetaData.InfiniteLoop -> {
                                while (true) {
                                    handleInstructionGroup(
                                        group = block,
                                        shapes = shapes,
                                        floatVariableStates = floatVariableStates,
                                        lines = lines,
                                        onError = onError
                                    )

                                    println("--------------------------------------")
                                    println("OUTPUT OF INSTRUCTION GROUP: ${block.id}")
                                    println("OUTPUT: Shapes: ${shapes.map { it.value }}")
                                    println("OUTPUT: Floats: ${floatVariableStates.keys}")
                                    println("--------------------------------------")
                                }
                            }

                            is InstructionGroupMetaData.Conditional -> {
                                val condition = block.metaData.condition
                                error("Conditional block are not yet supported!")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Exception: ${e.stackTraceToString()}")
        }
    }
}

suspend fun handleSingleInstruction(
    instruction: Instruction,
    shapes: SnapshotStateMap<String, UiShape>,
    lines: SnapshotStateMap<String, UiLine>,
    floatVariableStates: SnapshotStateMap<String, MutableFloatState>,
    onError: (Exception) -> Unit
) {
    try {
        when (instruction) {
            is Instruction.Variables.DefineInteger -> TODO()
            is Instruction.Variables.DefineString -> TODO()
            is Instruction.Visuals.DrawShape -> {

                shapes[instruction.name.value] = UiShape(
                    color = instruction.color.value.toLong(16),
                    width = instruction.width.value.extractValue(floatVariableStates, onError),
                    height = instruction.height.value.extractValue(floatVariableStates, onError),
                    cornerRadius = instruction.cornerRadius.value.extractValue(floatVariableStates, onError),
                    rotation = instruction.rotation.value.extractValue(floatVariableStates, onError),
                    scale = instruction.scaleField.value.extractValue(floatVariableStates, onError),
                    x = instruction.x.value.extractValue(floatVariableStates, onError),
                    y = instruction.y.value.extractValue(floatVariableStates, onError)
                )
            }

            is Instruction.Visuals.DrawLine -> {

                lines[UUID.randomUUID().toString().take(10)] = UiLine(
                    startX = instruction.startX.value.extractValue(floatVariableStates, onError),
                    startY = instruction.startY.value.extractValue(floatVariableStates, onError),
                    endX = instruction.endX.value.extractValue(floatVariableStates, onError),
                    endY = instruction.endY.value.extractValue(floatVariableStates, onError),
                    thickness = instruction.thickness.value.extractValue(floatVariableStates, onError),
                    color = instruction.color.value.toLong(16),
                )
            }

            is Instruction.Variables.DefineFloat -> {
                val floatName = instruction.nameField.value
                if (floatVariableStates.contains(floatName)) {
                    floatVariableStates[floatName]?.floatValue =
                        instruction.valueField.value.toFloat()
                } else {
                    floatVariableStates[instruction.nameField.value] =
                        mutableFloatStateOf(instruction.valueField.value.toFloat())
                }
            }

            is Instruction.Variables.ChangeFloat -> {
                val floatName = instruction.nameField.value
                val floatVariable =
                    floatVariableStates[floatName] ?: error(UNDEFINED_VARIABLE.format(floatName))

                val duration = instruction.durationField.value.toInt()
                val newValue = floatVariable.floatValue + instruction.deltaField.value.toFloat()
                if (duration == 0) {
                    floatVariableStates[floatName]!!.floatValue += newValue
                } else {
                    animate(
                        initialValue = floatVariable.floatValue,
                        targetValue = newValue,
                        animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                    ) { value, _ ->
                        floatVariable.floatValue = value
                    }
                }
            }

            is Instruction.Animations.AnimateFloat -> {
                val floatName = instruction.nameField.value
                val floatVariable =
                    floatVariableStates[floatName] ?: error(UNDEFINED_VARIABLE.format(floatName))
                animate(
                    initialValue = floatVariable.floatValue,
                    targetValue = instruction.valueField.value.toFloat(),
                    animationSpec = tween(
                        durationMillis = instruction.durationField.value.toInt(),
                        easing = LinearEasing
                    )
                ) { value, _ ->
                    floatVariable.floatValue = value
                }
            }

            is Instruction.Controls.Wait -> {
                delay(instruction.durationField.value.toLong())
            }

        }

    } catch (e: Exception) {
        println("e2: ${e.stackTraceToString()}")
    }
}

@Preview
@Composable
private fun OutputScreenPrev() {
    Column {
        OutputScreen(
            mainInstructionGroup = InstructionBlock.InstructionGroup(
                parentId = "",
                metaData = InstructionGroupMetaData.Thread
            ),
            onFinish = {}, onDrag = {}, onError = {}
        )
    }
}