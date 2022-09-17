package com.jsdisco.musichords.presentation.chordsinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.domain.models.ChordSearchResult
import com.jsdisco.musichords.ui.theme.Black500

@Composable
fun ChordResult(
    chordSearchResult: ChordSearchResult
) {

    val noteNames = stringArrayResource(id = R.array.notes_list_chordresult).toList()
    val textNotes = stringResource(id = R.string.chords_info_notes)
    val textHalftones = stringResource(id = R.string.chords_info_halftones)
    val textMidiKeys = stringResource(id = R.string.chords_info_midikeys)
    
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Black500)
        .padding(10.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.width(100.dp)) {
                Text(text = textNotes, fontWeight = FontWeight.Bold)
                chordSearchResult.noteStringIndices.forEach {index ->
                    Text(text = noteNames[index])
                }
            }
            Column(modifier = Modifier.width(100.dp)) {
                Text(text = textHalftones, fontWeight = FontWeight.Bold)
                chordSearchResult.intervals.forEach {
                    Text(text = it.toString())
                }
            }
            Column(modifier = Modifier.width(100.dp)) {
                Text(text = textMidiKeys, fontWeight = FontWeight.Bold)
                chordSearchResult.midiKeys.forEach {
                    Text(text = it.toString())
                }
            }
        }
    }
}