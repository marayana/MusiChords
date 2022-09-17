package com.jsdisco.musichords.presentation.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.jsdisco.musichords.ui.theme.*


object ButtonColour {
    val correctBg: Color = Green
    val incorrectBg: Color = Red
    val transparentBorderColour: Color = Transparent
}

class ButtonState(
    val text: String,
    val stringIndex: Int,
    initialBorderColour: Color = ButtonColour.transparentBorderColour,
    initialIsEnabled: Boolean = false
) {
    var borderColour by mutableStateOf(initialBorderColour)
    var isEnabled by mutableStateOf(initialIsEnabled)
}