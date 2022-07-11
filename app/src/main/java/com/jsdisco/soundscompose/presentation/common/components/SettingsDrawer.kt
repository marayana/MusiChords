package com.jsdisco.soundscompose.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.ui.theme.DTFontLight

@Composable
fun SettingsDrawer() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
    ) {
        Image(
            painter = painterResource(id = R.drawable.settingsplaceholder),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Settings",
            color = DTFontLight,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
    }
}