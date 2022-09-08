package com.jsdisco.soundscompose.presentation.settings_chords_create_exercise

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.local.models.ChordsSet
import com.jsdisco.soundscompose.data.models.Chord
import com.jsdisco.soundscompose.domain.models.SelectChordBtnState
import com.jsdisco.soundscompose.presentation.chords.ChordsViewModel
import com.jsdisco.soundscompose.presentation.common.components.SelectChordButton
import com.jsdisco.soundscompose.ui.theme.PinkLight
import com.jsdisco.soundscompose.ui.theme.Transparent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File.separator

private fun showToast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsChordsCreateExercise(
    viewModel: ChordsViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier,
        snackbar = {snackbarData: SnackbarData ->
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Top){
                Card(shape = RoundedCornerShape(8.dp), backgroundColor = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()) { Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Rounded.Error, contentDescription = "", tint = MaterialTheme.colors.onSecondary)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = snackbarData.message)
                }}
            }

        }
    )

    val context = LocalContext.current
    val toastEnterTitle = stringResource(id = R.string.settings_chords_new_exercise_toast_enter_title)
    val toastTooFewChords = stringResource(id = R.string.settings_chords_new_exercise_toast_too_few_chords)
    val toastTooManyChords = stringResource(id = R.string.settings_chords_new_exercise_toast_too_many_chords)
    val toastSaveSuccess = stringResource(id = R.string.settings_chords_new_exercise_toast_save_success)
    val toastExerciseExists = stringResource(id = R.string.settings_chords_new_exercise_toast_exercise_exists)

    val keyboardController = LocalSoftwareKeyboardController.current


    val chordNames = stringArrayResource(id = R.array.chords_list).toList()
    val chords = viewModel.allChords

    var exerciseTitle by rememberSaveable { mutableStateOf("") }

    val handleTitle = { title: String -> exerciseTitle = title}

    val initialSelected = chords.map { SelectChordBtnState(it.name) }

    val selectedChords by rememberSaveable { mutableStateOf(initialSelected) }

    fun toggleSelectChord(chord: Chord){
        val maximumChordsReached = selectedChords.filter { it.isSelected }.size >= 12

        selectedChords.forEach { c ->
            if (c.name == chord.name ){
                if (maximumChordsReached && !c.isSelected){
                    showToast(context, toastTooManyChords)
                } else {
                    c.isSelected = !c.isSelected
                    c.borderColour = if (c.isSelected) PinkLight else Transparent
                }
            }
        }
    }

    fun saveExercise(){
        if (exerciseTitle.isBlank()){
            //showToast(context, toastEnterTitle)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = toastEnterTitle,
                    actionLabel = "ok"
                )
            }
        } else {
            val selected = selectedChords.filter { it.isSelected }
            if (selected.size >= 2){
                val lowercaseExerciseTitle = exerciseTitle[0].lowercase() + exerciseTitle.slice(1 until exerciseTitle.length)

                if (viewModel.chordsSets.value.find { it.name == lowercaseExerciseTitle } != null){
                    showToast(context, toastExerciseExists)
                } else {
                    viewModel.addChordsSet(lowercaseExerciseTitle, selected)
                    keyboardController?.hide()
                    showToast(context, toastSaveSuccess)
                    exerciseTitle = ""
                    selectedChords.forEach { c ->
                        if (c.isSelected){
                            c.isSelected = false
                            c.borderColour = Transparent
                        }
                    }
                }


            } else {
                showToast(context, toastTooFewChords)
            }
        }
    }

    val title = stringResource(id = R.string.title_settings_chords_create_exercise)
    LaunchedEffect(Unit){
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
        ::saveExercise
    )
}

@Composable
fun SettingsChordsCreateExerciseUI(
    exerciseTitle: String,
    handleTitle: (String) -> Unit,
    selectedChords: List<SelectChordBtnState>,
    toggleSelectChord: (Chord) -> Unit,
    chordNames: List<String>,
    chords: List<Chord>,
    saveExercise: () -> Unit
) {

    val exerciseTitlePlaceholder = stringResource(id = R.string.settings_chords_new_exercise_title_placeholder)
    val exerciseTitleLabel = stringResource(id = R.string.settings_chords_new_exercise_title_label)
    val chordsLabel = stringResource(id = R.string.settings_chords_new_exercise_chords_label)
    val chordsDescription = stringResource(id = R.string.settings_chords_new_exercise_chords_description)


    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
        .padding(top = 20.dp, bottom = 70.dp)) {

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
                placeholder = { Text(text = exerciseTitlePlaceholder)},
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
                    contentDescription = "save exercise",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
            }
        }

        /*Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = chordsLabel,
            style = MaterialTheme.typography.h5
        )*/

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = selectedChords.filter { it.isSelected }
                .joinToString(separator = "     ") { it.name.replace("sharp", "#") },
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
        LazyColumn(modifier = Modifier.fillMaxSize()){
            itemsIndexed(chordsChunked){index, chordList ->
                ChordBtnsRow(
                    chordList,
                    chordNamesChunked[index],
                    selectedChordsChunked[index],
                    toggleSelectChord
                )
            }
        }

    }

}

@Composable
fun ChordBtnsRow(
    chordsRow: List<Chord>,
    chordNamesRow: List<String>,
    selectedChordsRow: List<SelectChordBtnState>,
    toggleSelectChord: (Chord) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until 3){
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)) {
                if (i < chordsRow.size){
                    SelectChordButton(
                        text = chordNamesRow[i],
                        borderColour = selectedChordsRow[i].borderColour,
                        onClick = { toggleSelectChord(chordsRow[i]) }
                    )
                }

            }
        }
    }
}