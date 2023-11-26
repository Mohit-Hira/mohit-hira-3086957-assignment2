package com.example.draughts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
class SettingsActivity : ComponentActivity() {
    private lateinit var sharedPrefHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefHelper = SharedPreferenceHelper(this)

        setContent {

            SettingsScreen(sharedPrefHelper)
        }
    }
}

