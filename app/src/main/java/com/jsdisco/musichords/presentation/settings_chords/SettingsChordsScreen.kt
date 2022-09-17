package com.jsdisco.musichords.presentation.settings_chords

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.presentation.chords.ChordsViewModel
import com.jsdisco.musichords.ui.theme.PinkLight

@Composable
fun SettingsChordsScreen(
    viewModel: ChordsViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    val isCheckedChordBtns: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(viewModel.settingsChords.value.playChordOnTap)
    }
    val isCheckedInversions: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(viewModel.settingsChords.value.withInversions)
    }
    val isCheckedOpenPosition: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(viewModel.settingsChords.value.withOpenPosition)
    }



    fun togglePlayChordOnTap() {
        isCheckedChordBtns.value = !isCheckedChordBtns.value
        viewModel.togglePlayChordOnTap()
    }

    fun toggleInversions(){
        isCheckedInversions.value = !isCheckedInversions.value
        viewModel.toggleInversions()
    }

    fun toggleOpenPosition(){
        isCheckedOpenPosition.value = !isCheckedOpenPosition.value
        viewModel.toggleOpenPosition()
    }




    val title = stringResource(id = R.string.title_settings_chords)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
    }

    SettingsChordsScreenUI(
        isCheckedChordBtns.value,
        isCheckedInversions.value,
        isCheckedOpenPosition.value,
        ::togglePlayChordOnTap,
        ::toggleInversions,
        ::toggleOpenPosition,

    )
}

@Composable
fun SettingsChordsScreenUI(
    isCheckedChordBtns: Boolean,
    isCheckedInversions: Boolean,
    isCheckedOpenPosition: Boolean,
    togglePlayChordOnTap: () -> Unit,
    toggleInversions: () -> Unit,
    toggleOpenPosition: () -> Unit,

) {

    val textPlayChords = stringResource(R.string.settings_chords_play_chords)
    val textPlayChordsLabel = stringResource(R.string.settings_chords_play_chords_label)
    val textInversions = stringResource(R.string.settings_chords_inversions)
    val textInversionsLabel = stringResource(R.string.settings_chords_inversions_label)
    val textOpenPosition = stringResource(R.string.settings_chords_open_position)
    val textOpenPositionLabel = stringResource(R.string.settings_chords_open_position_label)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 65.dp)
    ) {
        Text(
            text = textPlayChords,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = PinkLight),
                checked = isCheckedChordBtns,
                onCheckedChange = { togglePlayChordOnTap() }
            )
            Text(text = textPlayChordsLabel)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = textInversions,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = PinkLight),
                checked = isCheckedInversions,
                onCheckedChange = { toggleInversions() }
            )
            Text(text = textInversionsLabel)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = textOpenPosition,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = PinkLight),
                checked = isCheckedOpenPosition,
                onCheckedChange = { toggleOpenPosition() }
            )
            Text(text = textOpenPositionLabel)
        }
    }
}



