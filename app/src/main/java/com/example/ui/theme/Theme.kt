package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElectricPurpleBright,
    onPrimary = Color.White,
    primaryContainer = ElectricPurple,
    onPrimaryContainer = Color.White,
    secondary = NeonCoral,
    onSecondary = Color.White,
    secondaryContainer = HotPink,
    onSecondaryContainer = Color.White,
    tertiary = CyberTeal,
    onTertiary = Color.Black,
    background = DeepIndigoBg,
    onBackground = TextPrimaryLight,
    surface = DarkSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryLight,
    outline = DarkSurfaceCard
)

private val LightColorScheme = darkColorScheme( // For video call apps, rich dark/vibrant theme is standard
    primary = ElectricPurpleBright,
    onPrimary = Color.White,
    primaryContainer = ElectricPurple,
    onPrimaryContainer = Color.White,
    secondary = NeonCoral,
    onSecondary = Color.White,
    secondaryContainer = HotPink,
    onSecondaryContainer = Color.White,
    tertiary = CyberTeal,
    onTertiary = Color.Black,
    background = DeepIndigoBg,
    onBackground = TextPrimaryLight,
    surface = DarkSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryLight,
    outline = DarkSurfaceCard
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
