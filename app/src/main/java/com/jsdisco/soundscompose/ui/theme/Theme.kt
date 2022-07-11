package com.jsdisco.soundscompose.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import com.jsdisco.soundscompose.presentation.theme.Shapes

private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = DTBackground,
    onBackground = Color.White,
    surface = LightWhite,
    onSurface = DarkGray
)

private val LightColorPalette = lightColors(
    primary = Color.White,
    background = DTBackground,
    onBackground = Color.White,
    surface = LightWhite,
    onSurface = DarkGray
)

@Composable
fun SoundsComposeTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}