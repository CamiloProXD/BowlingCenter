
package com.example.bowlingcenter.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BowlingBlack   = Color(0xFF0D0D0D)
val BowlingRed     = Color(0xFFE53935)
val BowlingAmber   = Color(0xFFFFC107)
val BowlingWhite   = Color(0xFFF5F5F5)
val BowlingGray    = Color(0xFF1E1E1E)
val BowlingSurface = Color(0xFF2A2A2A)

private val DarkColors = darkColorScheme(
    primary       = BowlingRed,
    onPrimary     = Color.White,
    secondary     = BowlingAmber,
    onSecondary   = BowlingBlack,
    background    = BowlingBlack,
    onBackground  = BowlingWhite,
    surface       = BowlingGray,
    onSurface     = BowlingWhite,
    surfaceVariant = BowlingSurface
)

@Composable
fun BowlingTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}