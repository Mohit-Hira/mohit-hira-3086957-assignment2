package com.example.draughts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import java.lang.Math.abs


class MainActivity : ComponentActivity() {
    private var selectedBoxListOne = mutableStateOf<Pair<Int, Int>?>(null)

    private var customBoard= mutableStateListOf<MutableList<Int>>(
        mutableStateListOf(0, 1, 0, 1, 0, 1, 0, 1),
        mutableStateListOf(1, 0, 1, 0, 1, 0, 1, 0),
        mutableStateListOf(0, 1, 0, 1, 0, 1, 0, 1),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(2, 0, 2, 0, 2, 0, 2, 0),
        mutableStateListOf(0, 2, 0, 2, 0, 2, 0, 2),
        mutableStateListOf(2, 0, 2, 0, 2, 0, 2, 0),)

    private var selectedBoxList= mutableStateListOf<MutableList<Int>>(
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
        mutableStateListOf(0,0,0,0,0,0,0,0),
    )

    private var highlightedCells = mutableStateOf(listOf<Pair<Int, Int>>())
    private var isMoveDone=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val sharedPrefHelper = SharedPreferenceHelper(this)
            val onGoingPlayer = remember { mutableIntStateOf(2) }
            val playerTurn = remember { mutableStateOf("Turn: Player 1") }
            val player1Pieces = remember { mutableStateOf("Player 1's Remaining Pieces: 12") }
            val player2Pieces = remember { mutableStateOf("Player 1's Remaining Pieces: 12") }

            var tempCounterP1 = 0
            var tempCounterP2 = 0
            for (x in 0 until 8) {
                for (y in 0 until 8) {
                    if (customBoard[x][y] == 1 || customBoard[x][y] == 3)
                        tempCounterP1++
                    else if (customBoard[x][y] == 2 || customBoard[x][y] == 4)
                        tempCounterP2++
                }
            }
            player1Pieces.value = "Player 1 has $tempCounterP1 pieces left"
            player2Pieces.value = "Player 2 has $tempCounterP2 pieces left"
            if(tempCounterP1==0)
            {
                playerTurn.value = "Congratulations! Player 2 Won!"
            }
            else  if(tempCounterP2==0)
            {
                playerTurn.value = "Congratulations! Player 1 Won!"
            }


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        fontSize = 32.sp,
                        text = "Game of Draughts",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    DraughtsView(customBoard, selectedBoxList, highlightedCells.value, sharedPrefHelper)
                    { i, j ->
                        moveHandlerHelper(i, j, customBoard, onGoingPlayer, playerTurn)
                        for (x in 0 until 8) {
                            for (y in 0 until 8) {
                                if (x == i && y == j) {
                                    if(!isMoveDone)
                                        selectedBoxList[x][y] = 1
                                    else
                                        selectedBoxList[x][y]=0
                                } else
                                    selectedBoxList[x][y] = 0
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            fontSize = 20.sp,
                            text = playerTurn.value,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            fontSize = 18.sp,
                            text = player1Pieces.value,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            fontSize = 18.sp,
                            text = player2Pieces.value,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        )
                        {
                            Button(
                                onClick = {
                                    movedToSettings()
                                },
                                modifier = Modifier.size(width = 150.dp, height = 50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Color.DarkGray,
                                )
                            ) {
                                Text(
                                    color = Color.White,
                                    text="Settings",
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    reset(customBoard, onGoingPlayer, playerTurn) },
                                modifier = Modifier.size(width = 150.dp, height = 50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Color.DarkGray,
                                )
                            ) {
                                Text(
                                    color = Color.White,
                                    text="Reset Game",
                                )
                            }
                        }


                    }

                }
            }
        }
    }

    private fun movedToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

    }

    private fun switchingTurns(onGoingPlayer: MutableState<Int>) {

        onGoingPlayer.value = if (onGoingPlayer.value == 1) 2 else 1
    }

    private fun initializeBoard(customBoard: MutableList<MutableList<Int>>) {
        // Initialize game board with pieces
        customBoard.clear()
        val emptyRow = MutableList(8) { 0 }
        customBoard.addAll(List(3) { row -> MutableList(8) { col -> if ((row + col) % 2 == 0) 0 else 1 } })
        customBoard.addAll(List(2) { emptyRow.toMutableList() })
        customBoard.addAll(List(3) { row -> MutableList(8) { col -> if ((row + col) % 2 == 0) 2 else 0 } })
    }

    private fun reset(customBoard: MutableList<MutableList<Int>>,
                      onGoingPlayer: MutableState<Int>, playerTurn: MutableState<String>) {
        initializeBoard(customBoard)
        onGoingPlayer.value = 2
        playerTurn.value = "Turn: Player 1"

        for (i in 0 until 8) {
            for (j in 0 until 8) {
                selectedBoxList[i][j]=0
            }
        }
    }

    private fun moveHandlerHelper(x: Int, y: Int,
                                  customBoard: MutableList<MutableList<Int>>,
                                  onGoingPlayer: MutableState<Int>, playerTurn: MutableState<String>) {
        val piece = customBoard[y][x]

        isMoveDone=false
        // Determine the player of the piece
        val checkPlayer = if (piece in listOf(1, 3)) 1 else 2

        // Check if a piece of the current player is clicked
        if (checkPlayer == onGoingPlayer.value && piece != 0) {
            // Select the piece and highlight valid moves
            if (selectedBoxListOne.value == Pair(x, y)) {
//                // Deselect if the same piece is clicked again
//                selectedBoxListOne.value = null
//                highlightedCells.value = listOf()
            }
            else {
//                // Select a new piece and highlight valid moves
                selectedBoxListOne.value = Pair(x, y)
                highlightedCells.value = getAllValidMoves(Pair(x, y), customBoard)
            }
        }
        else{
//            selectedBoxListOne.value = null
//            highlightedCells.value = listOf()

        }
        // Check if it's the correct player's turn and the cell clicked is a valid move
        if (selectedBoxListOne.value != null && isMoveValid(x, y, selectedBoxListOne.value!!, customBoard)) {
//            selectedBoxListOne.value = Pair(x, y)
            movingPiece(x, y, selectedBoxListOne.value!!, customBoard)
            Log.d("DraughtsGame", "Move made to: $x, $y")
            isMoveDone=true
            switchingTurns(onGoingPlayer)
            if(onGoingPlayer.value==1) {
                playerTurn.value = "Turn: Player 2"
            }
            else{
                playerTurn.value = "Turn: Player 1"
            }
            selectedBoxListOne.value = null
            highlightedCells.value = listOf()
            return
        }

    }

    private fun isMoveValid(x: Int, y: Int, selectedBoxListOne: Pair<Int, Int>,
                            customBoard: MutableList<MutableList<Int>>): Boolean {
        // Get the list of all valid moves for the selectedBoxList piece
        val validMoves = getAllValidMoves(selectedBoxListOne, customBoard)

        // Check if the target move is in the list of valid moves
        return Pair(x, y) in validMoves
    }


    private fun isMoveValidHelper(x: Int, y: Int, selectedBoxListOne: Pair<Int, Int>,
                                  customBoard: MutableList<MutableList<Int>>): Boolean {
        val (fromX, fromY) = selectedBoxListOne
        if (x !in 0 until 8 || y !in 0 until 8) {
            // If the move is outside the board, it's not valid
            return false
        }

        val player = customBoard[fromY][fromX]
        val player_ = if (player == 1 || player == 3) 1 else 2
        val opponent = if (player == 1 || player == 3) 2 else 1

        val targetCell = customBoard[y][x]

        // Check if the target cell is empty
        if (targetCell != 0) return false

        // Calculate distance
        val dx = x - fromX
        val dy = y - fromY
        Log.d("DraughtsGame", "Checking valid move for king at $fromX, $fromY to $x, $y")

        // King movement logic
        if (player == 3 || player == 4) {
            Log.d("DraughtsGame", "Moving king at $fromX, $fromY to $x, $y")

            // King can move one space in any direction
            if (abs(dx) == 1 && abs(dy) == 1) return true

            // King capturing move
            if (abs(dx) == 2 && abs(dy) == 2) {
                val midX = (fromX + x) / 2
                val midY = (fromY + y) / 2
                val midCell = customBoard[midY][midX]
                return midCell != 0 && (midCell == opponent || midCell == opponent + 2)
                // Must jump over opponent's piece
            }
        }
        // Regular piece movement logic
        else {
            // Regular move
            if (abs(dx) == 1 && ((player_ == 1 && dy == 1) || (player_ == 2 && dy == -1))) {
                return true
            }
            // Regular capturing move
            if (abs(dx) == 2 && ((player_ == 1 && dy == 2) || (player_ == 2 && dy == -2))) {
                val midX = (fromX + x) / 2
                val midY = (fromY + y) / 2
                val midCell = customBoard[midY][midX]

//                return midCell != 0 && midCell != player_
                // Check if the middle cell contains an opponent's piece or king
                return midCell != 0 && (midCell == opponent || midCell == opponent + 2)

            }
        }

        return false
    }

    private fun movingPiece(x: Int, y: Int, selectedBoxListOne: Pair<Int, Int>,
                            customBoard: MutableList<MutableList<Int>>) {
        val (fromX, fromY) = selectedBoxListOne
        val player = customBoard[fromY][fromX]
        customBoard[fromY][fromX] = 0
        if (player == 3 || player == 4) {
            if (handlingCaptures(fromX, fromY, x, y, customBoard, player)) {
                checkingCaptures(x, y, customBoard, player)
            }
        }  else {
            if (handlingCaptures(fromX, fromY, x, y, customBoard, player)) {
                checkingCaptures(x, y, customBoard, player)
            }
        }
        changingPiece(x, y, customBoard, player)
    }

    private fun handlingCaptures(fromX: Int, fromY: Int, toX: Int, toY: Int, customBoard: MutableList<MutableList<Int>>, player: Int): Boolean {
        var currentX = fromX
        var currentY = fromY
        var captured = false

        while (currentX != toX || currentY != toY) {
            val directionX = if (toX > currentX) 1 else -1
            val directionY = if (toY > currentY) 1 else -1
            val nextX = currentX + directionX
            val nextY = currentY + directionY

            // Check for capture
            if (customBoard[nextY][nextX] != 0 && customBoard[nextY][nextX] % 2 != player % 2) {
                customBoard[nextY][nextX] = 0
                captured = true
            }

            currentX = nextX
            currentY = nextY
        }

        return captured
    }

    private fun checkingCaptures(x: Int, y: Int, customBoard: MutableList<MutableList<Int>>, player: Int): Boolean {
        val directions = listOf(-2, 2) // Capture moves are two steps away
        var additionalCaptureAvailable = false

        for (dirY in directions) {
            for (dirX in directions) {
                val newX = x + dirX
                val newY = y + dirY
                val midX = x + dirX / 2
                val midY = y + dirY / 2

                if (newX in 0 until 8 && newY in 0 until 8 && customBoard[newY][newX] == 0 && customBoard[midY][midX] != 0 && customBoard[midY][midX] % 2 != player % 2) {
                    // Temporarily make the capture
                    val capturedPiece = customBoard[midY][midX]
                    customBoard[midY][midX] = 0

                    if (handlingCaptures(x, y, newX, newY, customBoard, player)) {
                        additionalCaptureAvailable = true
                        // Recursive call if another capture is made
                        checkingCaptures(newX, newY, customBoard, player)
                    }

                    // Undo the capture for further exploration
                    customBoard[midY][midX] = capturedPiece
                }
            }
        }

        return additionalCaptureAvailable
    }

    private fun changingPiece(x: Int, y: Int,
                              customBoard: MutableList<MutableList<Int>>, player: Int) {
        customBoard[y][x] = player
        if ((player == 1 && y == 7) || (player == 2 && y == 0)) {
            val kingValue = if (player == 1) 3 else 4
            customBoard[y][x] = kingValue
        }
    }



    private fun getAllValidMoves(selectedBoxListOne: Pair<Int, Int>,
                                 customBoard: MutableList<MutableList<Int>>):
            List<Pair<Int, Int>> {
        val (x, y) = selectedBoxListOne
        val player = customBoard[y][x]
        val opponent = if(player==1||player==3)2 else 1
        val validMoves = mutableListOf<Pair<Int, Int>>()

        // Regular moves for kings
        if (player == 3 || player == 4) {
            for (dirY in listOf(-1, 1)) {
                for (dirX in listOf(-1, 1)) {
                    val newX = x + dirX
                    val newY = y + dirY
                    if (isMoveValidHelper(newX, newY, selectedBoxListOne, customBoard)) {
                        validMoves.add(Pair(newX, newY))
                    }
                }
            }
        }
        // Regular moves for non-kings
        else {
            val forwardDirection = if (player == 1) 1 else -1
            for (dirX in listOf(-1, 1)) {
                val newX = x + dirX
                val newY = y + forwardDirection
                if (isMoveValidHelper(newX, newY, selectedBoxListOne, customBoard)) {
                    validMoves.add(Pair(newX, newY))
                }
            }
        }

        // Add capturing moves for both kings and non-kings
        validMoves.addAll(getCaptureMoves(x, y, customBoard, opponent,player, mutableListOf()))

        return validMoves.distinct()
    }

    private fun getCaptureMoves(x: Int, y: Int,
                                customBoard: MutableList<MutableList<Int>>,
                                opponent: Int,
                                player: Int,
                                visited: MutableList<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val additionalCaptures = mutableListOf<Pair<Int, Int>>()
        val isKing = player == 3 || player == 4

        // Set capture directions based on piece type
        val captureDirectionsY = if (isKing) listOf(-2, 2) else if (player == 1) listOf(2) else listOf(-2)
        val captureDirectionsX = listOf(-2, 2) // Horizontal capture directions are the same for all

        for (dirY in captureDirectionsY) {
            for (dirX in captureDirectionsX) {
                val newX = x + dirX
                val newY = y + dirY
                val midX = (x + newX) / 2
                val midY = (y + newY) / 2

                // Check for valid capture conditions
                if (newX in 0 until 8 && newY in 0 until 8 &&
                    customBoard[newY][newX] == 0 && customBoard[midY][midX] != 0 && customBoard[midY][midX] % 2 != player % 2 &&
                    Pair(newX, newY) !in visited) {

                    // Store the original piece before capture
                    val originalPiece = customBoard[midY][midX]

                    // Temporarily make the capture
                    customBoard[midY][midX] = 0
                    visited.add(Pair(newX, newY))

                    // Recursively check for further captures from the new position
                    additionalCaptures.add(Pair(newX, newY))
                    additionalCaptures.addAll(getCaptureMoves(newX, newY, customBoard, opponent, player, ArrayList(visited)))

                    // Restore the original piece after exploring further captures
                    customBoard[midY][midX] = originalPiece
                    visited.remove(Pair(newX, newY))
                }
            }
        }

        return additionalCaptures.distinct()
    }

}








