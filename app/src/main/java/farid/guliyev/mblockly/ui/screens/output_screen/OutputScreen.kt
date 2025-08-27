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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import farid.guliyev.mblockly.domain.UNDEFINED_VARIABLE
import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.ui.model.UiShape
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlock
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionGroupMetaData
import farid.guliyev.mblockly.ui.screens.output_screen.components.OutputTopBar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.OutputScreen(
    mainInstructionGroup: InstructionBlock.InstructionGroup,
    onFinish: () -> Unit,
    currentDrag: Float = 0.5F,
    onError: (Exception) -> Unit,
    onDrag: (amount: Float) -> Unit
) {
    val shapes = remember { mutableStateMapOf<String, UiShape>() }
    val floatVariableStates = remember { mutableStateMapOf<String, MutableFloatState>() }
    LaunchedEffect(Unit) {
        try {
            handleInstructionGroup(group = mainInstructionGroup, shapes = shapes, floatVariableStates = floatVariableStates)
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
                        val newDrag = (currentDrag - dragAmount.y / (screenHeight.toPx())).coerceIn(0.1F, 0.99F)
                        println("newDrag: $newDrag")
                        onDrag(newDrag)
                    }
                },
                onFinish = onFinish
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            // DRAW SHAPES
            shapes.values.forEach { shape ->
                Canvas(modifier = Modifier) {
                    scale(scale = shape.scale.floatValue) {
                        translate(left = shape.x.floatValue, top = shape.y.floatValue) {
                            drawRoundRect(
                                color = Color(shape.color),
                                size = Size(shape.width.floatValue, shape.height.floatValue),
                                cornerRadius = CornerRadius(
                                    x = shape.cornerRadius.floatValue,
                                    y = shape.cornerRadius.floatValue
                                )
                            )
                        }
                    }
                }
            }


        }
    }
}

suspend fun handleInstructionGroup(
    group: InstructionBlock.InstructionGroup,
    shapes: SnapshotStateMap<String, UiShape>,
    floatVariableStates: SnapshotStateMap<String, MutableFloatState>
) {
    coroutineScope {
        group.instructionBlocks.forEach { block->
            when (block) {
                is InstructionBlock.SingleInstruction -> {
                    handleSingleInstruction(
                        instruction = block.instruction.base,
                        shapes = shapes,
                        floatVariableStates = floatVariableStates
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
                                    floatVariableStates = floatVariableStates
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
                                    floatVariableStates = floatVariableStates
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
                                    floatVariableStates = floatVariableStates
                                )

                                println("--------------------------------------")
                                println("OUTPUT OF INSTRUCTION GROUP: ${block.id}")
                                println("OUTPUT: Shapes: ${shapes.map { it.value }}")
                                println("OUTPUT: Floats: ${floatVariableStates.keys}")
                                println("--------------------------------------")
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun handleSingleInstruction(
    instruction: Instruction,
    shapes: SnapshotStateMap<String, UiShape>,
    floatVariableStates: SnapshotStateMap<String, MutableFloatState>
    ) {
    when (instruction) {
        is Instruction.Variables.DefineInteger -> TODO()
        is Instruction.Variables.DefineString -> TODO()
        is Instruction.Visuals.DrawShape -> {

            shapes[instruction.name.value] = UiShape(
                color = instruction.color.value.toLong(16),
                width = instruction.width.value.extractValue(floatVariableStates),
                height = instruction.height.value.extractValue(floatVariableStates),
                cornerRadius = instruction.cornerRadius.value.extractValue(floatVariableStates),
                scale = instruction.scaleField.value.extractValue(floatVariableStates),
                x = instruction.x.value.extractValue(floatVariableStates),
                y = instruction.y.value.extractValue(floatVariableStates)
            )
        }

        is Instruction.Variables.DefineFloat -> {
            val floatName = instruction.nameField.value
            if (floatVariableStates.contains(floatName)) {
                floatVariableStates[floatName]?.floatValue = instruction.valueField.value.toFloat()
            } else {
                floatVariableStates[instruction.nameField.value] = mutableFloatStateOf(instruction.valueField.value.toFloat())
            }
        }

        is Instruction.Variables.ChangeFloat -> {
            val floatName = instruction.nameField.value
            val floatVariable = floatVariableStates[floatName] ?: error(UNDEFINED_VARIABLE.format(floatName))

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
            val floatVariable = floatVariableStates[floatName] ?: error(UNDEFINED_VARIABLE.format(floatName))
            animate(
                initialValue = floatVariable.floatValue,
                targetValue = instruction.valueField.value.toFloat(),
                animationSpec = tween(durationMillis = instruction.durationField.value.toInt(), easing = LinearEasing)
            ) { value, _ ->
                floatVariable.floatValue = value
            }
        }

        is Instruction.Controls.Wait -> {
            delay(instruction.durationField.value.toLong())
        }

    }
}

@Preview
@Composable
private fun OutputScreenPrev() {
    Column {
        OutputScreen(
            mainInstructionGroup = InstructionBlock.InstructionGroup(parentId = "", metaData = InstructionGroupMetaData.Thread),
            onFinish = {}, onDrag = {}, onError = {}
        )
    }
}