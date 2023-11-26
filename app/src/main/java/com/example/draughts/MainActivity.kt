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

    private var validMovesToGo = mutableStateOf(listOf<Pair<Int, Int>>())
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

                    DraughtsView(customBoard, selectedBoxList, validMovesToGo.value, sharedPrefHelper)
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

        for (x in 0 until 8) {
            for (y in 0 until 8) {
                selectedBoxList[x][y]=0
            }
        }
    }

    private fun moveHandlerHelper(x: Int, y: Int,
                                  customBoard: MutableList<MutableList<Int>>,
                                  onGoingPlayer: MutableState<Int>, playerTurn: MutableState<String>) {
        val piece = customBoard[y][x]
        isMoveDone=false
        val checkPlayer = if (piece in listOf(1, 3)) 1 else 2

        if (checkPlayer == onGoingPlayer.value && piece != 0) {
            if (selectedBoxListOne.value == Pair(x, y)) {

            }
            else {
                selectedBoxListOne.value = Pair(x, y)
                validMovesToGo.value = getAllValidMoves(Pair(x, y), customBoard)
            }
        }
        else{

        }
        if (selectedBoxListOne.value != null && isMoveValid(x, y, selectedBoxListOne.value!!, customBoard)) {
            movingPiece(x, y, selectedBoxListOne.value!!, customBoard)
            isMoveDone=true
            switchingTurns(onGoingPlayer)
            if(onGoingPlayer.value==1) {
                playerTurn.value = "Turn: Player 2"
            }
            else{
                playerTurn.value = "Turn: Player 1"
            }
            selectedBoxListOne.value = null
            validMovesToGo.value = listOf()
            return
        }

    }

    private fun isMoveValid(i: Int, j: Int, selectedBoxListOne: Pair<Int, Int>,
                            customBoard: MutableList<MutableList<Int>>): Boolean {
        val validMoves = getAllValidMoves(selectedBoxListOne, customBoard)

        return Pair(i, j) in validMoves
    }


    private fun isMoveValidHelper(x: Int, y: Int, selectedBoxListOne: Pair<Int, Int>,
                                  customBoard: MutableList<MutableList<Int>>): Boolean {
        val (from_x, from_y) = selectedBoxListOne
        if (x !in 0 until 8 || y !in 0 until 8) {
            return false
        }

        val player = customBoard[from_y][from_x]
        val playerType = if (player == 1 || player == 3) 1 else 2
        val opponent = if (player == 1 || player == 3) 2 else 1

        val targetCell = customBoard[y][x]

        if (targetCell != 0) return false

        val distanceX = x - from_x
        val distanceY = y - from_y

        if (player == 3 || player == 4) {

            if (abs(distanceX) == 1 && abs(distanceY) == 1) return true

            if (abs(distanceX) == 2 && abs(distanceY) == 2) {
                val mid_x = (from_x + x) / 2
                val mid_y = (from_y + y) / 2
                val midBox = customBoard[mid_y][mid_x]
                return midBox != 0 && (midBox == opponent || midBox == opponent + 2)
            }
        }
        else {
            if (abs(distanceX) == 1 && ((playerType == 1 && distanceY == 1) || (playerType == 2 && distanceY == -1))) {
                return true
            }
            if (abs(distanceX) == 2 && ((playerType == 1 && distanceY == 2) || (playerType == 2 && distanceY == -2))) {
                val mid_x = (from_x + x) / 2
                val mid_y = (from_y + y) / 2
                val midBox = customBoard[mid_y][mid_x]

                return midBox != 0 && (midBox == opponent || midBox == opponent + 2)

            }
        }

        return false
    }

    private fun movingPiece(x: Int, y: Int, selectedBoxListOne: Pair<Int, Int>,
                            customBoard: MutableList<MutableList<Int>>) {
        val (from_x, from_y) = selectedBoxListOne
        val player = customBoard[from_y][from_x]
        customBoard[from_y][from_x] = 0
        if (player == 3 || player == 4) {
            if (handlingCaptures(from_x, from_y, x, y, customBoard, player)) {
                checkingCaptures(x, y, customBoard, player)
            }
        }  else {
            if (handlingCaptures(from_x, from_y, x, y, customBoard, player)) {
                checkingCaptures(x, y, customBoard, player)
            }
        }
        changingPiece(x, y, customBoard, player)
    }

    private fun handlingCaptures(from_x: Int, from_y: Int, to_x: Int, to_y: Int,
                                 customBoard: MutableList<MutableList<Int>>, player: Int): Boolean {
        var currentX = from_x
        var currentY = from_y
        var captured = false

        while (currentX != to_x || currentY != to_y) {
            val directionX = if (to_x > currentX) 1 else -1
            val directionY = if (to_y > currentY) 1 else -1
            val nextX = currentX + directionX
            val nextY = currentY + directionY

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
        val directions = listOf(-2, 2)
        var additionalCaptureAvailable = false

        for (dirY in directions) {
            for (dirX in directions) {
                val new_x = x + dirX
                val new_y = y + dirY
                val mid_x = x + dirX / 2
                val mid_y = y + dirY / 2

                if (new_x in 0 until 8 && new_y in 0 until 8 && customBoard[new_y][new_x] == 0 && customBoard[mid_y][mid_x] != 0 && customBoard[mid_y][mid_x] % 2 != player % 2) {
                    val capturedPiece = customBoard[mid_y][mid_x]
                    customBoard[mid_y][mid_x] = 0

                    if (handlingCaptures(x, y, new_x, new_y, customBoard, player)) {
                        additionalCaptureAvailable = true
                        checkingCaptures(new_x, new_y, customBoard, player)
                    }

                    customBoard[mid_y][mid_x] = capturedPiece
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

        if (player == 3 || player == 4) {
            for (dirY in listOf(-1, 1)) {
                for (dirX in listOf(-1, 1)) {
                    val new_x = x + dirX
                    val new_y = y + dirY
                    if (isMoveValidHelper(new_x, new_y, selectedBoxListOne, customBoard)) {
                        validMoves.add(Pair(new_x, new_y))
                    }
                }
            }
        }
        else {
            val forwardDirection = if (player == 1) 1 else -1
            for (dirX in listOf(-1, 1)) {
                val new_x = x + dirX
                val new_y = y + forwardDirection
                if (isMoveValidHelper(new_x, new_y, selectedBoxListOne, customBoard)) {
                    validMoves.add(Pair(new_x, new_y))
                }
            }
        }

        validMoves.addAll(getCaptureMoves(x, y, customBoard, opponent,player, mutableListOf()))

        return validMoves.distinct()
    }

    private fun getCaptureMoves(x: Int, y: Int,
                                customBoard: MutableList<MutableList<Int>>,
                                opponent: Int,
                                player: Int,
                                visited: MutableList<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val addCaptures = mutableListOf<Pair<Int, Int>>()
        val isKing = player == 3 || player == 4

        val captureDirectionsY = if (isKing) listOf(-2, 2) else if (player == 1) listOf(2) else listOf(-2)
        val captureDirectionsX = listOf(-2, 2)

        for (dirY in captureDirectionsY) {
            for (dirX in captureDirectionsX) {
                val new_x = x + dirX
                val new_y = y + dirY
                val mid_x = (x + new_x) / 2
                val mid_y = (y + new_y) / 2

                if (new_x in 0 until 8 && new_y in 0 until 8 &&
                    customBoard[new_y][new_x] == 0 && customBoard[mid_y][mid_x] != 0 && customBoard[mid_y][mid_x] % 2 != player % 2 &&
                    Pair(new_x, new_y) !in visited) {

                    val originalPiece = customBoard[mid_y][mid_x]

                    customBoard[mid_y][mid_x] = 0
                    visited.add(Pair(new_x, new_y))

                    addCaptures.add(Pair(new_x, new_y))
                    addCaptures.addAll(getCaptureMoves(new_x, new_y, customBoard, opponent, player, ArrayList(visited)))

                    customBoard[mid_y][mid_x] = originalPiece
                    visited.remove(Pair(new_x, new_y))
                }
            }
        }

        return addCaptures.distinct()
    }

}








