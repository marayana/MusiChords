package com.jsdisco.musichords.presentation.settings_chords_create_exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.data.models.Chord
import com.jsdisco.musichords.domain.models.SelectBtnState
import com.jsdisco.musichords.presentation.chords.ChordsViewModel
import com.jsdisco.musichords.presentation.common.components.SelectButton
import com.jsdisco.musichords.ui.theme.PinkLight
import com.jsdisco.musichords.ui.theme.Transparent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File.separator


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsChordsCreateExercise(
    viewModel: ChordsViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showSnackbarString by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current


    val chordNames = stringArrayResource(id = R.array.chords_list).toList()
    val chords = viewModel.allChords

    var exerciseTitle by rememberSaveable { mutableStateOf("") }

    val handleTitle = { title: String -> exerciseTitle = title }

    val initialSelected = chords.map { SelectBtnState(it.name) }

    val selectedChords by rememberSaveable { mutableStateOf(initialSelected) }

    val toastEnterTitle =
        stringResource(id = R.string.settings_chords_new_exercise_toast_enter_title)
    val toastTooFewChords =
        stringResource(id = R.string.settings_chords_new_exercise_toast_too_few_chords)
    val toastTooManyChords =
        stringResource(id = R.string.settings_chords_new_exercise_toast_too_many_chords)
    val toastSaveSuccess =
        stringResource(id = R.string.settings_chords_new_exercise_toast_save_success)
    val toastExerciseExists =
        stringResource(id = R.string.settings_chords_new_exercise_toast_exercise_exists, exerciseTitle)


    fun toggleSelectChord(chord: Chord) {
        val maximumChordsReached = selectedChords.filter { it.isSelected }.size >= 12

        selectedChords.forEach { c ->
            if (c.name == chord.name) {
                if (maximumChordsReached && !c.isSelected) {
                    showSnackbarString = toastTooManyChords
                } else {
                    c.isSelected = !c.isSelected
                    c.borderColour = if (c.isSelected) PinkLight else Transparent
                }
            }
        }
    }

    fun resetSnackbarString(){
        showSnackbarString = ""
    }

    fun saveExercise() {
        if (exerciseTitle.isBlank()) {
            showSnackbarString = toastEnterTitle
        } else {
            val selected = selectedChords.filter { it.isSelected }
            if (selected.size >= 2) {
                val lowercaseExerciseTitle =
                    exerciseTitle[0].lowercase() + exerciseTitle.slice(1 until exerciseTitle.length)

                if (viewModel.chordsSets.value.find { it.name == lowercaseExerciseTitle } != null) {
                    showSnackbarString = toastExerciseExists
                } else {
                    viewModel.addChordsSet(lowercaseExerciseTitle, selected)
                    keyboardController?.hide()
                    showSnackbarString = toastSaveSuccess
                    exerciseTitle = ""
                    selectedChords.forEach { c ->
                        if (c.isSelected) {
                            c.isSelected = false
                            c.borderColour = Transparent
                        }
                    }
                }


            } else {
                showSnackbarString = toastTooFewChords
            }
        }
    }

    val title = stringResource(id = R.string.title_settings_chords_create_exercise)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
    }

    SettingsChordsCreateExerciseUI(
        exerciseTitle,
        handleTitle,
        selectedChords,
        ::toggleSelectChord,
        chordNames,
        chords,
        ::saveExercise,
        snackbarHostState,
        scope,
        showSnackbarString,
        ::resetSnackbarString
    )
}

@Composable
fun SettingsChordsCreateExerciseUI(
    exerciseTitle: String,
    handleTitle: (String) -> Unit,
    selectedChords: List<SelectBtnState>,
    toggleSelectChord: (Chord) -> Unit,
    chordNames: List<String>,
    chords: List<Chord>,
    saveExercise: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    showSnackbarString: String,
    resetSnackbarString: () -> Unit
) {

    val toastOk = stringResource(id = R.string.settings_chords_new_exercise_toast_ok)

    if (showSnackbarString != ""){
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = showSnackbarString,
                    actionLabel = toastOk
                )

            }
        }
        resetSnackbarString()
    }

    val exerciseTitlePlaceholder =
        stringResource(id = R.string.settings_chords_new_exercise_title_placeholder)
    val exerciseTitleLabel = stringResource(id = R.string.settings_chords_new_exercise_title_label)
    val chordsDescription =
        stringResource(id = R.string.settings_chords_new_exercise_chords_description)
    val textSaveExercise = stringResource(id = R.string.settings_chords_new_exercise_save)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 20.dp, bottom = 70.dp)
        ) {

            Text(
                text = exerciseTitleLabel,
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier.height(60.dp),
                    placeholder = { Text(text = exerciseTitlePlaceholder) },
                    singleLine = true,
                    maxLines = 1,
                    value = exerciseTitle,
                    onValueChange = handleTitle
                )
                Button(
                    modifier = Modifier.height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSecondary
                    ),
                    onClick = saveExercise
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = textSaveExercise,
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val selectedChordNames = mutableListOf<String>()
            selectedChords.forEachIndexed { index, chord ->
               if (chord.isSelected){
                   selectedChordNames.add(chordNames[index])
               }
            }

            Text(
                text = selectedChordNames.joinToString(separator = "     "),
                color = PinkLight,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = chordsDescription,
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(10.dp))

            val chordsChunked = chords.chunked(3)
            val chordNamesChunked = chordNames.chunked(3)
            val selectedChordsChunked = selectedChords.chunked(3)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(chordsChunked) { index, chordList ->
                    ChordBtnsRow(
                        chordList,
                        chordNamesChunked[index],
                        selectedChordsChunked[index],
                        toggleSelectChord
                    )
                }
            }

        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier,
            snackbar = { snackbarData: SnackbarData ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = MaterialTheme.colors.surface,
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Error,
                                contentDescription = "",
                                tint = MaterialTheme.colors.onSecondary
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = snackbarData.message,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun ChordBtnsRow(
    chordsRow: List<Chord>,
    chordNamesRow: List<String>,
    selectedChordsRow: List<SelectBtnState>,
    toggleSelectChord: (Chord) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until 3) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                if (i < chordsRow.size) {
                    SelectButton(
                        text = chordNamesRow[i],
                        borderColour = selectedChordsRow[i].borderColour,
                        onClick = { toggleSelectChord(chordsRow[i]) }
                    )
                }
            }
        }
    }
}