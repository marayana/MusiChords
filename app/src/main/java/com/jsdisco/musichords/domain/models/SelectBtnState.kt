package com.jsdisco.musichords.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jsdisco.musichords.ui.theme.Transparent

class SelectBtnState(
    val name: String,
){
    var isSelected by mutableStateOf(false)
    var borderColour by mutableStateOf(Transparent)
}