package com.jsdisco.musichords.presentation.chordsinfo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.domain.models.ChordSearchResult
import com.jsdisco.musichords.data.models.Note
import com.jsdisco.musichords.ui.theme.Black900
import com.jsdisco.musichords.ui.theme.Blue
import com.jsdisco.musichords.ui.theme.BlueLight
import com.jsdisco.musichords.ui.theme.White900


@Composable
fun Piano(notesWhite: List<Note>, notesBlack: List<Note>, activeChord: ChordSearchResult) {

    Column(modifier = Modifier.fillMaxWidth().height(105.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp)){
            Canvas(modifier = Modifier.matchParentSize()){
                val numKeys = 14f
                val keyWhiteW = (size.width / numKeys)
                val keyWhiteH = size.height
                val keyBlackW = keyWhiteW * 0.8f
                val keyBlackH = keyWhiteH * 0.6f
                val cornerSize = 3.dp
                val strokeWidth = 5f
                for((i, note) in notesWhite.slice(0..13).withIndex()){

                    val keyColour = when(activeChord.midiKeysPiano.indexOf(note.midiKey)){
                        0 -> Blue
                        -1 -> White900
                        else -> BlueLight
                    }

                    val path = Path()
                    path.moveTo(i.toFloat() * keyWhiteW, 0f)
                    path.lineTo((i+1).toFloat() * keyWhiteW, 0f)
                    path.lineTo((i+1).toFloat() * keyWhiteW, keyWhiteH)
                    //path.lineTo((i+1).toFloat() * keyWhiteW, keyWhiteH - cornerSize)
                    /*path.arcTo(
                        rect = Rect(topLeft = Offset(-cornerSize, 0f), bottomRight = Offset(0f, cornerSize)),
                        startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = true
                    )*/
                    path.lineTo(i.toFloat() * keyWhiteW, keyWhiteH)
                    path.close()
                    drawPath(path = path, color = Black900, style=Stroke(width = strokeWidth, pathEffect = PathEffect.cornerPathEffect(cornerSize.toPx())))
                    drawRoundRect(
                        color = keyColour,
                        topLeft = Offset(i.toFloat() * keyWhiteW, 0f),
                        size = Size(keyWhiteW, keyWhiteH),
                        cornerRadius = CornerRadius(cornerSize.toPx())
                    )
                }

                val noBlackIndices = listOf(2, 6, 9 )
                val offset = keyWhiteW - (keyBlackW/2f)
                var blackNotesIndex = 0
                for (i in (0..12)){
                    if (noBlackIndices.contains(i)){
                        continue
                    }

                    val note = notesBlack[blackNotesIndex]
                    val keyColour = when(activeChord.midiKeysPiano.indexOf(note.midiKey)){
                        0 -> Blue
                        -1 -> Black900
                        else -> BlueLight
                    }
                    drawRect(
                        color = keyColour,
                        topLeft = Offset(offset + i.toFloat() * keyWhiteW, -strokeWidth/2),
                        size = Size(keyBlackW, 0.2f * keyBlackH)
                    )
                    drawRoundRect(
                        color = keyColour,
                        topLeft = Offset(offset + i.toFloat() * keyWhiteW, 0f),
                        size = Size(keyBlackW, keyBlackH),
                        cornerRadius = CornerRadius(cornerSize.toPx())
                    )
                    blackNotesIndex++
                }
            }
        }
    }
}
