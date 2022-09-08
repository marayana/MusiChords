package com.jsdisco.soundscompose.presentation.chordsinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.domain.models.ChordSearchResult

@Composable
fun ChordResult(
    chordSearchResult: ChordSearchResult
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0x33000000))
        .padding(10.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.width(100.dp)) {
                Text(text = "notes", fontWeight = FontWeight.Bold)
                chordSearchResult.noteNames.forEach {
                    Text(text = it.toString())
                }
            }
            Column(modifier = Modifier.width(100.dp)) {
                Text(text = "halftones", fontWeight = FontWeight.Bold)
                chordSearchResult.intervals.forEach {
                    Text(text = it.toString())
                }
            }
            Column(modifier = Modifier.width(100.dp)) {
                Text(text = "midi keys", fontWeight = FontWeight.Bold)
                chordSearchResult.midiKeys.forEach {
                    Text(text = it.toString())
                }
            }
        }
    }
}