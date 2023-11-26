package com.example.draughts

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.floor

@Composable
fun DraughtsView(customBoard: MutableList<MutableList<Int>>,
                 selectedPiece: MutableList<MutableList<Int>>,highlightValidMoves: List<Pair<Int, Int>>,
                 sharedPrefHelper: SharedPreferenceHelper,
                 onAttemptMove: (Int, Int) -> Unit) {
    var width by remember { mutableStateOf(0.0f) }
    var height by remember { mutableStateOf(0.0f) }
    var cellWidth by remember { mutableStateOf(0.0f) }
    var cellHeight by remember { mutableStateOf(0.0f) }
    val boardColor = Color(sharedPrefHelper.getBoardColor(Color.DarkGray.toArgb()))
    val player1Color = Color(sharedPrefHelper.getPlayer1PieceColor(Color.Black.toArgb()))
    val player2Color = Color(sharedPrefHelper.getPlayer2PieceColor(Color.White.toArgb()))

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
                var color = if ((x + y) % 2 == 0) Color.LightGray else boardColor
                if(selectedPiece[x][y]==1)
                    color=Color.Yellow
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
                        if (customBoard[x][y] == 1) player1Color
                        else if (customBoard[x][y] == 2) player2Color
                        else if (customBoard[x][y] == 3) player1Color
                        else player2Color
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
@Composable
fun SettingsScreen(sharedPrefHelper: SharedPreferenceHelper) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var boardColor by remember { mutableStateOf(Color(sharedPrefHelper.getBoardColor(Color.DarkGray.toArgb()))) }
    var player1Color by remember { mutableStateOf(Color(sharedPrefHelper.getPlayer1PieceColor(Color.Black.toArgb()))) }
    var player2Color by remember { mutableStateOf(Color(sharedPrefHelper.getPlayer2PieceColor(Color.White.toArgb()))) }

//    val defaultBoardColor = Color.DarkGray
//    val defaultPlayer1Color = Color.Black
//    val defaultPlayer2Color = Color.White
    val defaultBoardColor = Color(sharedPrefHelper.getBoardColor(Color.DarkGray.toArgb()))
    val defaultPlayer1Color = Color(sharedPrefHelper.getPlayer1PieceColor(Color.Black.toArgb()))
    val defaultPlayer2Color = Color(sharedPrefHelper.getPlayer2PieceColor(Color.White.toArgb()))
    // Initialize slider values with default color RGB components
    var redBoard by remember { mutableStateOf(boardColor.red) }
    var greenBoard by remember { mutableStateOf(boardColor.green) }
    var blueBoard by remember { mutableStateOf(boardColor.blue) }

    var redPieceP1 by remember { mutableStateOf(player1Color.red) }
    var greenPieceP1 by remember { mutableStateOf(player1Color.green) }
    var bluePieceP1 by remember { mutableStateOf(player1Color.blue) }

    var redPieceP2 by remember { mutableStateOf(player2Color.red) }
    var greenPieceP2 by remember { mutableStateOf(player2Color.green) }
    var bluePieceP2 by remember { mutableStateOf(player2Color.blue) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Board Color",  color = Color.Gray,
            fontSize = 16.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left)
        ColorPicker("Red", redBoard) { redBoard = it }
        ColorPicker("Green", greenBoard) { greenBoard = it }
        ColorPicker("Blue", blueBoard) { blueBoard = it }

        Spacer(modifier = Modifier.height(10.dp))

        // Color picker for Player 1 Pieces
        Text("Player 1 Piece Color",  color = Color.Gray,
            fontSize = 16.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start)
        ColorPicker("Red", redPieceP1) { redPieceP1 = it }
        ColorPicker("Green", greenPieceP1) { greenPieceP1 = it }
        ColorPicker("Blue", bluePieceP1) { bluePieceP1 = it }

        Spacer(modifier = Modifier.height(10.dp))

        // Color picker for Player 2 Pieces
        Text("Player 2 Piece Color",  color = Color.Gray,
            fontSize = 16.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start)
        ColorPicker("Red", redPieceP2) { redPieceP2 = it }
        ColorPicker("Green", greenPieceP2) { greenPieceP2 = it }
        ColorPicker("Blue", bluePieceP2) { bluePieceP2 = it }

        Spacer(modifier = Modifier.height(32.dp))

        // Finish button
        Button(onClick = {
            val boardColor_ = Color(redBoard, greenBoard, blueBoard).toArgb()
            sharedPrefHelper.saveBoardColor(boardColor_)
            boardColor = Color(boardColor_)
            val pieceColorP1 = Color(redPieceP1, greenPieceP1, bluePieceP1).toArgb()
            sharedPrefHelper.savePlayer1PieceColor(pieceColorP1)
            player1Color = Color(pieceColorP1)
            val pieceColorP2 = Color(redPieceP2, greenPieceP2, bluePieceP2).toArgb()
            sharedPrefHelper.savePlayer2PieceColor(pieceColorP2)
            player2Color = Color(pieceColorP2)
            if (context is Activity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                context.finish()
            }
        },
            modifier = Modifier.size(width = 150.dp, height = 50.dp).align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text("Save",
                color = Color.White,
                fontSize = 16.sp, // Adjust font size as needed
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ColorPicker(label: String, value: Float, onValueChange: (Float) -> Unit) {
    val intValue = (value * 255).toInt() // Convert to integer range (0-255)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label: $intValue", // Display label with current value
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.width(80.dp) // Set a fixed width for the label
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = Color.DarkGray, // Color of the thumb
                activeTrackColor = Color.Black, // Color of the active track
                inactiveTrackColor = Color.Gray // Color of the inactive track
            ),
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
//    Text(label, color = Color.Gray,
//        fontSize = 12.sp, )
//    Slider(value = value,
//        onValueChange = onValueChange,
//        colors = SliderDefaults.colors(
//            thumbColor = Color.DarkGray, // Color of the thumb (the draggable circle)
//            activeTrackColor = Color.Black, // Color of the active track
//            inactiveTrackColor = Color.Gray // Color of the inactive track
//        ),
//        modifier = Modifier.padding(horizontal = 5.dp))
}


