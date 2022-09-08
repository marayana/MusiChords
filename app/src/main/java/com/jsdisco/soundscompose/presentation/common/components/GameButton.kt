package com.jsdisco.soundscompose.presentation.common.components

import android.util.Log
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.presentation.common.ButtonState
import com.jsdisco.soundscompose.R

@Composable
fun GameButton(
    btnState: ButtonState,
    onClick: () -> Unit
) {
    val btnText = stringArrayResource(id = R.array.chords_list)[btnState.stringIndex]

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
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}