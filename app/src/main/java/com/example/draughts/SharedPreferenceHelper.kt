package com.example.draughts


import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("DraughtsPrefs", Context.MODE_PRIVATE)

    fun setBoardColor(color: Int) {
        prefs.edit().putInt("BoardColor", color).apply()
    }

    fun getBoardColor(defaultColor: Int): Int {
        return prefs.getInt("BoardColor", defaultColor)
    }


    fun setPlayer1PieceColor(color: Int) {
        prefs.edit().putInt("Player1PieceColor", color).apply()
    }

    fun getPlayer1PieceColor(defaultColor: Int): Int {
        return prefs.getInt("Player1PieceColor", defaultColor)
    }

    fun setPlayer2PieceColor(color: Int) {
        prefs.edit().putInt("Player2PieceColor", color).apply()
    }

    fun getPlayer2PieceColor(defaultColor: Int): Int {
        return prefs.getInt("Player2PieceColor", defaultColor)
    }
}
