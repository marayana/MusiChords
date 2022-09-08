package com.jsdisco.soundscompose.presentation.settings_relative_pitch

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.presentation.relativepitch.RelativePitchViewModel

@Composable
fun SettingsRelativePitchScreen(
    viewModel: RelativePitchViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    val title = stringResource(id = R.string.title_settings_relative_pitch)
    LaunchedEffect(Unit){
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
    }

    SettingsRelativePitchScreenUI()
}

@Composable
fun SettingsRelativePitchScreenUI(

) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp)
        .padding(top = 20.dp, bottom = 70.dp),) {
        Text(text = "RP SETTINGS")

        Text(text = "notation: halftones / full name / intervals = 2,3,3b..")
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "play with .. (intervals)")

        }
    }
}