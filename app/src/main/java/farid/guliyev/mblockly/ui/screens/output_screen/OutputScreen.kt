package farid.guliyev.mblockly.ui.screens.output_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import farid.guliyev.mblockly.domain.model.Instruction
import farid.guliyev.mblockly.ui.model.MovableUiShape
import farid.guliyev.mblockly.ui.model.toMovableShape
import farid.guliyev.mblockly.ui.screens.output_screen.components.OutputTopBar

@Composable
fun OutputScreen(
    instructions: List<Instruction>,
    onFinish: () -> Unit
) {
    val shapes = remember { mutableStateMapOf<String, MovableUiShape>() }
    LaunchedEffect(Unit) {
        for (instruction in instructions) {
            when (instruction) {
                is Instruction.Variables.DefineDouble -> TODO()
                is Instruction.Variables.DefineInteger -> TODO()
                is Instruction.Variables.DefineWord -> TODO()
                is Instruction.Visuals.DrawShape -> {
                    shapes[instruction.name.label] = instruction.toMovableShape()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            OutputTopBar(
                onFinish = onFinish
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            // DRAW SHAPES
            shapes.keys.forEach { name ->
                val movableShape = shapes[name]!!
                val shape = movableShape.shape
                Canvas(modifier = Modifier) {
                    translate(left = movableShape.x, top = movableShape.y) {
                        drawRoundRect(
                            color = Color(shape.color),
                            size = Size(shape.width, shape.height),
                            cornerRadius = CornerRadius(
                                x = shape.cornerRadius,
                                y = shape.cornerRadius
                            )
                        )
                    }
                }
            }


        }
    }
}

@Preview
@Composable
private fun OutputScreenPrev() {
    OutputScreen(
        instructions = listOf(
            Instruction.Visuals.DrawShape()
        ),
        onFinish = {}
    )
}