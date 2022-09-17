package com.jsdisco.musichords.presentation.settings_relative_pitch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.data.models.Interval
import com.jsdisco.musichords.domain.models.SelectBtnState
import com.jsdisco.musichords.presentation.common.components.SelectButton
import com.jsdisco.musichords.presentation.relativepitch.RelativePitchViewModel
import com.jsdisco.musichords.ui.theme.OrangeLight
import com.jsdisco.musichords.ui.theme.Transparent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsRelativePitchScreen(
    viewModel: RelativePitchViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showSnackbarString by remember { mutableStateOf("") }

    val toastTooFewIntervals = stringResource(id = R.string.settings_rp_toast_too_few_intervals)

    val intervalNames = if (viewModel.settingsRP.value.isNotationAbbr) stringArrayResource(id = R.array.intervals_list_abbr).toList() else stringArrayResource(id = R.array.intervals_list).toList()
    val intervals = viewModel.allIntervals

    val initialSelected = listOf(
        SelectBtnState(name = "1"),
        SelectBtnState(name = "2"),
        SelectBtnState(name = "3"),
        SelectBtnState(name = "4"),
        SelectBtnState(name = "5"),
        SelectBtnState(name = "6"),
        SelectBtnState(name = "7"),
        SelectBtnState(name = "8"),
        SelectBtnState(name = "9"),
        SelectBtnState(name = "10"),
        SelectBtnState(name = "11"),
        SelectBtnState(name = "12"),
    )
    initialSelected.forEachIndexed { i, btn ->
        val isSelected = viewModel.activeIntervals.value.contains(intervals[i])
        btn.isSelected = isSelected
        btn.borderColour = if (isSelected) OrangeLight else Transparent
    }

    val selectedIntervals by rememberSaveable {mutableStateOf(initialSelected)}

    val isCheckedIntervalBtns: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(viewModel.settingsRP.value.playIntervalOnTap)
    }

    val isCheckedCompoundIntervals: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(viewModel.settingsRP.value.withCompoundIntervals)
    }

    val isCheckedNotationAbbr: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(viewModel.settingsRP.value.isNotationAbbr)
    }

    fun togglePlayIntervalOnTap(){
        isCheckedIntervalBtns.value = !isCheckedIntervalBtns.value
        viewModel.togglePlayIntervalOnTap()
    }

    fun toggleCompoundIntervals(){
        isCheckedCompoundIntervals.value = !isCheckedCompoundIntervals.value
        viewModel.toggleCompoundIntervals()
    }

    fun toggleNotationAbbr(){
        isCheckedNotationAbbr.value = !isCheckedNotationAbbr.value
        viewModel.toggleNotationAbbr()
    }

    fun toggleSelectInterval(interval: Interval){
        val minimumIntervalsReached = selectedIntervals.filter { it.isSelected }.size <= 2

        selectedIntervals.forEach { int ->
            if (int.name.toInt() == interval.halfTones){
                if (minimumIntervalsReached && int.isSelected){
                    showSnackbarString = toastTooFewIntervals
                } else {
                    int.isSelected = !int.isSelected
                    int.borderColour = if (int.isSelected) OrangeLight else Transparent
                    viewModel.toggleActiveIntervals(interval)
                }
            }
        }
    }

    fun resetSnackbarString(){
        showSnackbarString = ""
    }

    val title = stringResource(id = R.string.title_settings_relative_pitch)
    LaunchedEffect(Unit){
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
    }

    SettingsRelativePitchScreenUI(
        isCheckedIntervalBtns.value,
        isCheckedCompoundIntervals.value,
        isCheckedNotationAbbr.value,
        intervals,
        selectedIntervals,
        intervalNames,
        ::togglePlayIntervalOnTap,
        ::toggleCompoundIntervals,
        ::toggleNotationAbbr,
        ::toggleSelectInterval,
        snackbarHostState,
        scope,
        showSnackbarString,
        ::resetSnackbarString
    )
}

@Composable
fun SettingsRelativePitchScreenUI(
    isCheckedIntervalBtns: Boolean,
    isCheckedCompoundIntervals: Boolean,
    isCheckedNotationAbbr: Boolean,
    intervals: List<Interval>,
    selectedIntervals: List<SelectBtnState>,
    intervalNames: List<String>,
    togglePlayIntervalOnTap: () -> Unit,
    toggleCompoundIntervals: () -> Unit,
    toggleNotationAbbr: () -> Unit,
    toggleSelectInterval: (Interval) -> Unit,
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


    val textPlayIntervals = stringResource(id = R.string.settings_rp_play_intervals)
    val textPlayIntervalsLabel = stringResource(id = R.string.settings_rp_play_intervals_label)
    val textCompoundIntervals = stringResource(id = R.string.settings_rp_compound_intervals)
    val textCompoundIntervalsLabel = stringResource(id = R.string.settings_rp_compound_intervals_label)
    val textNotationAbbr = stringResource(id = R.string.settings_rp_notation_abbr)
    val textNotationAbbrLabel = stringResource(id = R.string.settings_rp_notation_abbr_label)
    val textIntervals = stringResource(id = R.string.settings_rp_select_intervals)

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 65.dp)
            .verticalScroll(rememberScrollState())) {

            Text(
                text = textPlayIntervals,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    colors = CheckboxDefaults.colors(checkedColor = OrangeLight),
                    checked = isCheckedIntervalBtns,
                    onCheckedChange = {togglePlayIntervalOnTap()}
                )
                Text(text = textPlayIntervalsLabel)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = textCompoundIntervals,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    colors = CheckboxDefaults.colors(checkedColor = OrangeLight),
                    checked = isCheckedCompoundIntervals,
                    onCheckedChange = {toggleCompoundIntervals()}
                )
                Text(text = textCompoundIntervalsLabel)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = textNotationAbbr,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    colors = CheckboxDefaults.colors(checkedColor = OrangeLight),
                    checked = isCheckedNotationAbbr,
                    onCheckedChange = {toggleNotationAbbr()}
                )
                Text(text = textNotationAbbrLabel)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = textIntervals,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(15.dp))

            val intervalsChunked = intervals.chunked(3)
            val intervalNamesChunked = intervalNames.chunked(3)
            val selectedIntervalsChunked = selectedIntervals.chunked(3)
            Column(modifier = Modifier.fillMaxWidth()) {
                for ((index, intervalList) in intervalsChunked.withIndex()){
                    IntervalBtnsRow(
                        intervalList,
                        intervalNamesChunked[index],
                        selectedIntervalsChunked[index],
                        toggleSelectInterval
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
fun IntervalBtnsRow(
    intervalsRow: List<Interval>,
    intervalNamesRow: List<String>,
    selectedIntervalsRow: List<SelectBtnState>,
    toggleSelectInterval: (Interval) -> Unit
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
                if (i < intervalsRow.size){
                    SelectButton(
                        text = intervalNamesRow[i],
                        borderColour = selectedIntervalsRow[i].borderColour,
                        onClick = {toggleSelectInterval(intervalsRow[i])}
                    )
                }
            }
        }
    }
}