package com.jsdisco.musichords.presentation.chordsinfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.domain.models.ChordSearchResult
import com.jsdisco.musichords.data.models.Note
import com.jsdisco.musichords.presentation.chordsinfo.components.ChordResult
import com.jsdisco.musichords.presentation.chordsinfo.components.Piano
import com.jsdisco.musichords.presentation.common.components.DropDownList

@Composable
fun ChordsInfoScreen(
    viewModel: ChordsInfoViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {
    viewModel.loadSounds(LocalContext.current)


    val noteNames = stringArrayResource(id = R.array.notes_list).toList()
    val chordNames = stringArrayResource(id = R.array.chords_list).toList()
    val roots = viewModel.roots
    val chords = viewModel.chords

    val notesWhite = viewModel.notesWhite
    val notesBlack = viewModel.notesBlack

    // dropdowns
    var isOpenRoot by remember{mutableStateOf(false)}
    var isOpenChordName by remember{mutableStateOf(false)}

    var selectedRoot by rememberSaveable{mutableStateOf(noteNames[0])}
    var selectedChordName by rememberSaveable{mutableStateOf(chordNames[0])}

    fun handleRootDropDown(value: Boolean){ isOpenRoot = value }
    fun handleChordNameDropDown(value: Boolean){ isOpenChordName = value }

    fun selectRoot(rootName: String){
        selectedRoot = rootName
        val rootIndex = noteNames.indexOf(selectedRoot)
        val chordIndex = chordNames.indexOf(selectedChordName)
        viewModel.getChord(roots[rootIndex], chords[chordIndex].name)
    }
    fun selectChordName(chordName: String){
        selectedChordName = chordName
        val rootIndex = noteNames.indexOf(selectedRoot)
        val chordIndex = chordNames.indexOf(selectedChordName)
        viewModel.getChord(roots[rootIndex], chords[chordIndex].name)
    }

    fun playChord(){
        viewModel.playSounds()
    }

    val title = stringResource(id = R.string.title_chords_info)
    LaunchedEffect(Unit){
        onSetAppBarTitle(title)
        onSetShowBackBtn(false)
        viewModel.reset()
    }
    ChordsInfoScreenUI(
        noteNames,
        chordNames,
        notesWhite,
        notesBlack,
        isOpenRoot,
        isOpenChordName,
        selectedRoot,
        selectedChordName,
        viewModel.result,
        ::handleRootDropDown,
        ::handleChordNameDropDown,
        ::selectRoot,
        ::selectChordName,
        ::playChord)
}


@Composable
fun ChordsInfoScreenUI(
    noteNames: List<String>,
    chordNames: List<String>,
    notesWhite: List<Note>,
    notesBlack: List<Note>,
    isOpenRoot: Boolean,
    isOpenChordName: Boolean,
    selectedRoot: String,
    selectedChordName: String,
    resultChord: ChordSearchResult,
    handleRootDropDown: (Boolean) -> Unit,
    handleChordNameDropDown: (Boolean) -> Unit,
    selectRoot: (String) -> Unit,
    selectChordName: (String) -> Unit,
    playChord: () -> Unit
) {

    val textIconPlay = stringResource(id = R.string.chords_info_icon_play)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp, bottom = 65.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.fillMaxWidth(0.5f)){
                        DropDownList(isOpenRoot, noteNames, handleRootDropDown, selectRoot, selectedRoot)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.fillMaxWidth(1f)){
                        DropDownList(isOpenChordName, chordNames, handleChordNameDropDown, selectChordName, selectedChordName)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    ChordResult(chordSearchResult = resultChord)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    IconButton(
                        onClick = { playChord() },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = textIconPlay, tint = MaterialTheme.colors.onSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Piano(notesWhite = notesWhite, notesBlack = notesBlack, activeChord = resultChord)
                }
            }
        }
    }
}