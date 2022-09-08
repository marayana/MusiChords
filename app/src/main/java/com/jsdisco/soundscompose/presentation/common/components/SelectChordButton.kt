package com.jsdisco.soundscompose.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.presentation.common.ButtonState




@Composable
fun SelectChordButton(
    text: String,
    borderColour: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colors.secondary)
            .border(2.dp, borderColour, RoundedCornerShape(6.dp))
            .clickable { onClick() }
    ){
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}