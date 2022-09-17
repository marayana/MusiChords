package com.jsdisco.musichords.presentation.settings_chords_exercises

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsdisco.musichords.R
import com.jsdisco.musichords.data.local.models.ChordsSet
import com.jsdisco.musichords.data.models.Chord
import com.jsdisco.musichords.presentation.chords.ChordsViewModel
import com.jsdisco.musichords.presentation.common.navigation.GameScreen
import com.jsdisco.musichords.ui.theme.Black300
import com.jsdisco.musichords.ui.theme.Black400
import com.jsdisco.musichords.ui.theme.BlueLink
import com.jsdisco.musichords.ui.theme.PinkLight

@Composable
fun SettingsChordsExercises(
    viewModel: ChordsViewModel,
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    val selected: MutableState<String> = rememberSaveable {
        mutableStateOf(viewModel.activeChordsSet.value.name)
    }

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    var chordsSetToDelete by remember {
        mutableStateOf<ChordsSet?>(null)
    }


    fun selectChordsSet(set: ChordsSet) {
        selected.value = set.name
        viewModel.selectChordsSet(set)
    }

    fun deleteChordsSet(){
        if (chordsSetToDelete != null){
            viewModel.deleteChordsSet(chordsSetToDelete!!)
            if (chordsSetToDelete!!.name == selected.value){
                selectChordsSet(viewModel.chordsSets.value[0])
            }
        }
    }

    fun showDeleteDialog(){
        isDialogOpen = true
    }

    fun closeDeleteDialog(){
        isDialogOpen = false
    }

    fun setChordsSetToDelete(set: ChordsSet){
        chordsSetToDelete = set
    }

    val getChordsForChordsSet = {set: ChordsSet -> viewModel.getChordsForChordsSet(set) }

    val navToCreateExercise =
        { navController.navigate(GameScreen.SettingsChordsCreateExercise.route) }

    val title = stringResource(id = R.string.title_settings_chords_exercises)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
        viewModel.reset()
    }
    SettingsChordsExercisesUI(
        viewModel.chordsSets.value,
        getChordsForChordsSet,
        selected.value,
        ::selectChordsSet,
        ::deleteChordsSet,
        navToCreateExercise,
        isDialogOpen,
        ::showDeleteDialog,
        ::closeDeleteDialog,
        ::setChordsSetToDelete
    )

}

@Composable
fun SettingsChordsExercisesUI(
    chordsSets: List<ChordsSet>,
    getChordsForChordsSet: (ChordsSet) -> List<Chord>,
    selected: String,
    selectChordsSet: (ChordsSet) -> Unit,
    deleteChordsSet: () -> Unit,
    navToCreateExercise: () -> Unit,
    isDialogOpen: Boolean,
    showDeleteDialog: () -> Unit,
    closeDeleteDialog: () -> Unit,
    setChordsSetToDelete: (ChordsSet) -> Unit
) {

    val textSelectExercise = stringResource(R.string.settings_chords_select_exercise)
    val textCreateNew = stringResource(R.string.settings_chords_create_new_exercise)
    val textDialogDeleteTitle = stringResource(R.string.settings_chords_dialog_delete_title)
    val textDialogDeleteConfirm = stringResource(R.string.settings_chords_dialog_delete_confirm)
    val textDialogDeleteDismiss = stringResource(R.string.settings_chords_dialog_delete_dismiss)
    val nameStrings = stringArrayResource(id = R.array.settings_chords_chordgroup).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 70.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = textSelectExercise, style = MaterialTheme.typography.h5)

            TextButton(onClick = {navToCreateExercise()}){
                Text(
                    text = textCreateNew,
                    textAlign = TextAlign.End,
                    color = BlueLink
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(chordsSets){set ->
                RadioItem(
                    set = set,
                    nameStrings = nameStrings,
                    chords = getChordsForChordsSet(set),
                    selectedOption = selected,
                    onSelectedChange = selectChordsSet,
                    showDeleteDialog = showDeleteDialog,
                    setChordsSetToDelete = setChordsSetToDelete
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        if (isDialogOpen){
            AlertDialog(
                modifier = Modifier
                    .height(100.dp),
                onDismissRequest = { closeDeleteDialog() },
                title = {Text(text = textDialogDeleteTitle)},
                confirmButton = {
                    TextButton(
                        onClick = {
                        deleteChordsSet()
                        closeDeleteDialog()
                    }) {
                        Text(
                            text = textDialogDeleteConfirm,
                            color = BlueLink
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { closeDeleteDialog() }) {
                        Text(
                            text = textDialogDeleteDismiss,
                            color = BlueLink
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun RadioItem(
    set: ChordsSet,
    nameStrings: List<String>,
    chords: List<Chord>,
    selectedOption: String,
    onSelectedChange: (ChordsSet) -> Unit,
    showDeleteDialog: () -> Unit,
    setChordsSetToDelete: (ChordsSet) -> Unit
){
    var areChordsVisible by remember{mutableStateOf(false)}

    fun toggleChordsVisibility(){
        areChordsVisible = !areChordsVisible
    }

    val textDelete = stringResource(id = R.string.settings_chords_delete_exercise)
    val chordStrings = stringArrayResource(id = R.array.chords_list)
    val textExpand = stringResource(id = R.string.settings_chords_expand_exercise)
    val textCollapse = stringResource(id = R.string.settings_chords_collapse_exercise)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black400),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clickable { onSelectedChange(set) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(selectedColor = PinkLight),
                    selected = set.name == selectedOption,
                    onClick = { onSelectedChange(set) }
                )
                Text(
                    text = if (set.isCustom) set.name else nameStrings[set.stringIndex],
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Column {
            Icon(
                painterResource(id = if (areChordsVisible) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                contentDescription = if (areChordsVisible) textCollapse else textExpand,
                modifier = Modifier
                    .padding(10.dp)
                    .width(40.dp)
                    .height(40.dp)
                    .clickable { toggleChordsVisibility() }
            )
        }
    }

    AnimatedVisibility(
        visible = areChordsVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black300)
        ) {
            val chordsChunked = chords.chunked(2)

            for (chordPair in chordsChunked){
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = chordStrings[chordPair[0].stringIndex],
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp )
                        )
                    }
                    if (chordPair.size > 1){
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = chordStrings[chordPair[1].stringIndex],
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp )
                            )
                        }
                    }
                }
            }
            if (set.isCustom){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            setChordsSetToDelete(set)
                            showDeleteDialog()
                        },
                        modifier = Modifier.padding(10.dp)
                    ){
                        Text(text = textDelete, color = BlueLink)
                    }
                }
            }
        }
    }
}