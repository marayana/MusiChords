package com.jsdisco.soundscompose.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jsdisco.soundscompose.ui.theme.Transparent

class SelectChordBtnState(
    val name: String,
){
    var isSelected by mutableStateOf(false)
    var borderColour by mutableStateOf(Transparent)
}