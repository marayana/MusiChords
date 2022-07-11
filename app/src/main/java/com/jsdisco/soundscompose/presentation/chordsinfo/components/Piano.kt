package com.jsdisco.soundscompose.presentation.chordsinfo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.domain.models.FullChord
import com.jsdisco.soundscompose.data.models.Note


@Composable
fun Piano(notesWhite: List<Note>, notesBlack: List<Note>, activeChord: FullChord) {

    Column(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        Box(modifier = Modifier.fillMaxSize()){
            Canvas(modifier = Modifier.matchParentSize()){
                val numKeys = 14f
                val keyWhiteW = (size.width / numKeys)
                val keyWhiteH = size.height
                val keyBlackW = keyWhiteW * 0.8f
                val keyBlackH = keyWhiteH * 0.6f
                for((i, note) in notesWhite.slice(0..13).withIndex()){

                    val keyColour = when(activeChord.midiKeys.indexOf(note.midiKey)){
                        0 -> Color(0xFFAA2828)
                        -1 -> Color(0xCCFFFFFF)
                        else -> Color(0xFFBE5A5A)
                    }

                    val path = Path()
                    path.moveTo(i.toFloat() * keyWhiteW, 0f)
                    path.lineTo((i+1).toFloat() * keyWhiteW, 0f)
                    path.lineTo((i+1).toFloat() * keyWhiteW, keyWhiteH)
                    path.lineTo(i.toFloat() * keyWhiteW, keyWhiteH)
                    path.close()
                    drawPath(path = path, color = Color(0xFF000000), style=Stroke(5f))
                    drawRect(
                        color = keyColour,
                        topLeft = Offset(i.toFloat() * keyWhiteW, 0f),
                        size = Size(keyWhiteW, keyWhiteH),
                    )
                }
                val noBlackIndices = listOf(2, 6, 9 )
                val offset = keyWhiteW - (keyBlackW/2f)
                var blackNotesIndex = 0
                for (i in (0..12)){
                    // (i, note) in notesBlack.slice(0..9).withIndex()
                    if (noBlackIndices.contains(i)){
                        continue
                    }

                    val note = notesBlack[blackNotesIndex]
                    val keyColour = when(activeChord.midiKeys.indexOf(note.midiKey)){
                        0 -> Color(0xFFAA2828)
                        -1 -> Color(0xFF000000)
                        else -> Color(0xFFBE5A5A)
                    }
                    drawRect(
                        color = keyColour,
                        topLeft = Offset(offset + i.toFloat() * keyWhiteW, 0f),
                        size = Size(keyBlackW, keyBlackH)
                    )
                    blackNotesIndex++
                }
            }
        }
    }
}