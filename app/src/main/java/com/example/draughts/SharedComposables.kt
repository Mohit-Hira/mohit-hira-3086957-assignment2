package com.example.draughts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.floor


@Composable
fun DraughtsView(customBoard: MutableList<MutableList<Int>>,
                 selectedPiece: MutableList<MutableList<Int>>,highlightValidMoves: List<Pair<Int, Int>>,

                 onAttemptMove: (Int, Int) -> Unit) {
    var width by remember { mutableStateOf(0.0f) }
    var height by remember { mutableStateOf(0.0f) }
    var cellWidth by remember { mutableStateOf(0.0f) }
    var cellHeight by remember { mutableStateOf(0.0f) }

    Canvas(modifier = Modifier.aspectRatio(1.0f).pointerInput(Unit) {
        detectTapGestures(onTap = {
            val cellX = floor(it.x / cellWidth).toInt()
            val cellY = floor(it.y / cellHeight).toInt()
            onAttemptMove(cellX, cellY)
        })
    }) {
        width = size.width
        height = size.height
        cellWidth = width / 8.0f
        cellHeight = height / 8.0f

        // Draw 8x8 checkerboard
        for (x in 0 until 8) {
            for (y in 0 until 8) {
                var color = if ((x + y) % 2 == 0) Color.LightGray else Color.DarkGray
                if(selectedPiece[x][y]==1)
                    color= Color.Yellow
                drawRect(color, Offset(x * cellWidth, y * cellHeight), Size(cellWidth, cellHeight))
            }
        }
        for ((x, y) in highlightValidMoves) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(x * cellWidth, y * cellHeight),
                size = Size(cellWidth, cellHeight)
            )
        }

        for (x in 0 until 8) {
            for (y in 0 until 8) {
                if (customBoard[x][y] != 0) {
                    val color =
                        if (customBoard[x][y] == 1) Color.LightGray
                        else if (customBoard[x][y] == 2) Color.DarkGray
                        else if (customBoard[x][y] == 3) Color.LightGray
                        else Color.DarkGray
                    drawCircle(
                        color = color,
                        center = Offset(
                            y * cellWidth + cellWidth / 2,
                            x * cellHeight + cellHeight / 2
                        ),
                        radius = cellWidth / 2 * 0.8f
                    )
                    //for king
                    if (customBoard[x][y] == 3||customBoard[x][y] == 4) {
                        drawCircle(
                            color = Color.Red,
                            center = Offset(
                                x * cellWidth + cellWidth / 2,
                                y * cellHeight + cellHeight / 2
                            ),
                            radius = cellWidth / 4 * 0.8f
                        )
                    }

                }
            }
        }
    }
}