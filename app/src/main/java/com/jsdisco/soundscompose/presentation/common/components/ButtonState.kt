package com.jsdisco.soundscompose.presentation.common.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.jsdisco.soundscompose.presentation.common.components.ButtonColour.initialBg
import com.jsdisco.soundscompose.ui.theme.*


object ButtonColour {
    val initialBg: Color = DTGreyDark
    val disabledBg: Color = DTBtnDisabled
    val correctBg: Color = DTGreen
    val incorrectBg: Color = DTRed
    val textOnInitial: Color = DTFontLight
    val textOnColour: Color = DTFontDark
    val initialBorderColour: Color = DTTransparent
}

class ButtonState(
    val index: Int,
    val text: String,
    initialTextColour: Color = ButtonColour.textOnInitial,
    initialBg: Color = ButtonColour.initialBg,
    initialBorderColour: Color = ButtonColour.initialBorderColour,
    initialIsDisabled: Boolean = true
) {
    var textColour: Color = initialTextColour
    var bgColour by mutableStateOf(initialBg)
    var borderColour by mutableStateOf(initialBorderColour)
    var isDisabled by mutableStateOf(initialIsDisabled)
}