package com.jsdisco.soundscompose.presentation.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.jsdisco.soundscompose.ui.theme.*


object ButtonColour {
    val textDisabled: Color = Grey400
    val initialBg: Color = Grey800
    val disabledBg: Color = Grey500
    val correctBg: Color = Green
    val incorrectBg: Color = Red
    val transparentBorderColour: Color = Transparent
}

class ButtonState(
    val index: Int,
    val text: String,
    val stringIndex: Int,
    initialBorderColour: Color = ButtonColour.transparentBorderColour,
    initialIsEnabled: Boolean = false
) {
    var borderColour by mutableStateOf(initialBorderColour)
    var isEnabled by mutableStateOf(initialIsEnabled)
}