package com.jsdisco.soundscompose.presentation.chordsinfo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.domain.models.FullChord
import com.jsdisco.soundscompose.data.models.Note
import com.jsdisco.soundscompose.presentation.chordsinfo.components.ChordResult
import com.jsdisco.soundscompose.presentation.chordsinfo.components.Piano
import com.jsdisco.soundscompose.presentation.common.components.DropDownList

@Composable
fun ChordsInfoScreen(
    viewModel: ChordsInfoViewModel,
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit
) {
    viewModel.loadSounds(LocalContext.current)

    val roots = viewModel.roots
    val chords = viewModel.chords
    val chordNames = chords.map { it.name.replace("sharp", "#") }
    val notesWhite = viewModel.notesWhite
    val notesBlack = viewModel.notesBlack

    // dropdowns
    var isOpenRoot by remember{mutableStateOf(false)}
    var isOpenChordName by remember{mutableStateOf(false)}

    var selectedRoot by rememberSaveable{mutableStateOf("C")}
    var selectedChordName by rememberSaveable{mutableStateOf("major")}

    fun handleRootDropDown(value: Boolean){ isOpenRoot = value }
    fun handleChordNameDropDown(value: Boolean){ isOpenChordName = value }

    //var result by remember{mutableStateOf(viewModel.getChord(selectedRoot, selectedChordName))}
    val result = viewModel.result
    fun selectRoot(root: String){
        selectedRoot = root
        //result = viewModel.getChord(selectedRoot, selectedChordName)
        viewModel.getChord(selectedRoot, selectedChordName)
    }
    fun selectChordName(chordName: String){
        selectedChordName = chordName
        //result = viewModel.getChord(selectedRoot, selectedChordName)
        viewModel.getChord(selectedRoot, selectedChordName)
    }

    fun playChord(){
        viewModel.playSounds()
    }

    LaunchedEffect(Unit){
        onSetAppBarTitle("Chords Info")
        viewModel.reset()
    }
    ChordsInfoScreenUI(
        roots,
        chordNames,
        notesWhite,
        notesBlack,
        isOpenRoot,
        isOpenChordName,
        selectedRoot,
        selectedChordName,
        result,
        ::handleRootDropDown,
        ::handleChordNameDropDown,
        ::selectRoot,
        ::selectChordName,
        ::playChord)
}


@Composable
fun ChordsInfoScreenUI(
    roots: List<String>,
    chordNames: List<String>,
    notesWhite: List<Note>,
    notesBlack: List<Note>,
    isOpenRoot: Boolean,
    isOpenChordName: Boolean,
    selectedRoot: String,
    selectedChordName: String,
    resultChord: FullChord,
    handleRootDropDown: (Boolean) -> Unit,
    handleChordNameDropDown: (Boolean) -> Unit,
    selectRoot: (String) -> Unit,
    selectChordName: (String) -> Unit,
    playChord: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select root note and chord name:",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.fillMaxWidth(0.5f)){
                        DropDownList(isOpenRoot, roots, handleRootDropDown, selectRoot, selectedRoot)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.fillMaxWidth(1f)){
                        DropDownList(isOpenChordName, chordNames, handleChordNameDropDown, selectChordName, selectedChordName)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    ChordResult(fullChord = resultChord)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    Image(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "play sound",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { playChord()}
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Piano(notesWhite = notesWhite, notesBlack = notesBlack, activeChord = resultChord)
                }
            }
        }
    }
}