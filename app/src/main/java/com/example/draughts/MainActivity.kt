package com.example.draughts

import android.content.Intent
import android.os.Bundle
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

class MainActivity : ComponentActivity(){
    //1 for player 2 and 2 for player 1
    private var customBoard =
        mutableStateListOf<MutableList<Int>>(
            mutableStateListOf(0, 1, 0, 1, 0, 1, 0, 1),
            mutableStateListOf(1, 0, 1, 0, 1, 0, 1, 0),
            mutableStateListOf(0, 1, 0, 1, 0, 1, 0, 1),
            mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0),
            mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0),
            mutableStateListOf(2, 0, 2, 0, 2, 0, 2, 0),
            mutableStateListOf(0, 2, 0, 2, 0, 2, 0, 2),
            mutableStateListOf(2, 0, 2, 0, 2, 0, 2, 0),
            )

    private var isMoveDone=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val onGoingPlayer = remember { mutableIntStateOf(2) }
            val playerTurn = remember { mutableStateOf("Player 1's turn") }
            val player1Pieces = remember { mutableStateOf("Player 1's Remaining Pieces: 12") }
            val player2Pieces = remember { mutableStateOf("Player 1's Remaining Pieces: 12") }

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

               //draughts view
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
                                    //reset
                                },

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

}