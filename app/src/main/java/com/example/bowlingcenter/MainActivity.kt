package com.example.bowlingcenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.bowlingcenter.ui.navigation.AppNavigation
import com.example.bowlingcenter.ui.theme.BowlingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Extiende el contenido debajo de la barra de estado
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BowlingTheme {
                AppNavigation()
            }
        }
    }
}