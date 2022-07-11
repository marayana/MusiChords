package com.jsdisco.soundscompose.presentation.chordsinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsdisco.soundscompose.domain.models.FullChord

@Composable
fun ChordResult(
    fullChord: FullChord
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0x33000000))
        .padding(10.dp)
    ){
        Row(){
            Text(
                text = "${fullChord.root} ${fullChord.chordName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.width(100.dp)) {
                Text(text = "intervals", fontWeight = FontWeight.Bold)
                fullChord.intervals.forEach {
                    Text(text = it.toString())
                }
            }
            Column(modifier = Modifier.width(100.dp)) {
                Text(text = "notes", fontWeight = FontWeight.Bold)
                fullChord.noteNames.forEach {
                    Text(text = it.toString())
                }
            }
            Column(modifier = Modifier.width(100.dp)) {
                Text(text = "midi keys", fontWeight = FontWeight.Bold)
                fullChord.midiKeys.forEach {
                    Text(text = it.toString())
                }
            }
        }
    }
}