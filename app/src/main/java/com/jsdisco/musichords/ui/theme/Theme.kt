package com.jsdisco.musichords.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.jsdisco.musichords.presentation.theme.Shapes

private val DarkColorPalette = darkColors(
    primary = Grey100,
    onPrimary = Black800,
    secondary = Grey800,
    onSecondary = Grey100,
    background = Grey600,
    onBackground = Grey100,
    surface = Grey700,
    onSurface = Grey200
)

private val LightColorPalette = lightColors(
    primary = Grey900,
    onPrimary = White100,
    secondary = Grey100,
    onSecondary = Grey900,
    background = White100,
    onBackground = Black800,
    surface = Grey100,
    onSurface = Grey900,
)

@Composable
fun MusiChordsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}