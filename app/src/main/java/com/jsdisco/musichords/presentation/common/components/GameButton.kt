package com.jsdisco.musichords.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.presentation.common.ButtonState

@Composable
fun GameButton(
    btnState: ButtonState,
    btnText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colors.secondary)
            .border(2.dp, btnState.borderColour, RoundedCornerShape(6.dp))
            .clickable(enabled = btnState.isEnabled) { onClick() }
    ){
        Text(
            text = btnText,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(Alignment.Center)
        )
    }
}