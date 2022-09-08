package com.jsdisco.soundscompose.presentation.chords.components

import android.graphics.BitmapFactory
import android.util.Half.toFloat
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Chord
import com.jsdisco.soundscompose.data.models.Note
import com.jsdisco.soundscompose.data.models.Root
import com.jsdisco.soundscompose.data.models.Scale
import com.jsdisco.soundscompose.domain.models.ChordSolution
import com.jsdisco.soundscompose.presentation.chords.GameStatus
import org.koin.core.KoinApplication.Companion.init
import java.util.Collections.max
import kotlin.math.ceil
import kotlin.math.floor

enum class Accidental { NONE, SHARP, FLAT, DOUBLESHARP, DOUBLEFLAT }

data class SheetNote(
    val name: String,
    val baseMk: Int,
    var lineIndex: Int = 0,
    var transX: Float = 0f,
    var transY: Float = 0f,
    var acc: Accidental,
    var accDx: Float = 0f
)

class SheetDimensions(dens: Float) {
    val noteTransX = 85f * dens // base shift to the right
    val noteDx = 15f * dens // shifting further to the right for notes that are on the next line

    val linesDistance = 4.5f * dens // distance between C and D
    val ledgerLineDx = 2.4f * dens // shifting to the left so the line and the note don't align left

    val lowestLine = 111 * dens // A
    val lowestNoteY = lowestLine + 3 - linesDistance // A

    val clefDx = 11 * dens
    val clefDy = 39 * dens

    val accDxs = listOf(15f * dens, 28f * dens, 41f * dens)
    val accDy = 5f * dens
}

data class LedgerLine(
    val dx: Float,
    val dy: Float
)

class Sheet(
    val roots: List<Root>,
    val scales: List<Scale>,
    val solution: ChordSolution,
    val sheetDimensions: SheetDimensions
) {

    val linesDy = mutableListOf<Float>()
    val ledgerLinesTop = mutableListOf<LedgerLine>()
    val ledgerLinesBottom = mutableListOf<LedgerLine>()

    var sheetNotes by mutableStateOf(getSheetNotes(roots, scales, solution, sheetDimensions))

    init {
        initLinesDy()
    }

    private fun initLinesDy() {
        for (i in 2 until 7) {
            linesDy.add(sheetDimensions.lowestLine - i * 2 * sheetDimensions.linesDistance)
        }
    }

    fun updateSheetNotes(
        roots: List<Root>,
        scales: List<Scale>,
        solution: ChordSolution,
        sheetDimensions: SheetDimensions
    ) {
        ledgerLinesTop.clear()
        ledgerLinesBottom.clear()
        sheetNotes = getSheetNotes(roots, scales, solution, sheetDimensions)
    }

    private fun getNote(
        interval: Int,
        scale: Scale,
        solution: ChordSolution
    ): SheetNote {

        val noteName: String

        if (interval == 2){
            // second note: sus2
            noteName = scale.major[1]
        } else if (interval == 3 && !solution.chord.isMajor){
            // secondNote: minor
            noteName = scale.minor[2]
        } else if (interval == 3 && solution.chord.isMajor){
            // secondNote: dim
            noteName = scale.minor[2]
        } else if (interval == 4){
            // second note: third
            noteName = scale.major[2]
        } else if (interval == 5){
            // second note: sus4
            noteName = scale.major[3]
        } else if (interval == 6){
            // thirdNote: dim or b5
            val fifth = scale.major[4]
            noteName = if (fifth.contains("#")) fifth[0].toString() else fifth + "b"
        } else if (interval == 7){
            // third note: perfect fifth
            noteName = scale.major[4]
        } else if (interval == 8){
            // thirdNote: aug or #5
            val fifth = scale.major[4]
            noteName = if (fifth.contains("b")) fifth[0].toString() else "$fifth#"
        } else if (interval == 9 && solution.chord.name.contains("dim")){
            // fourth note: dim7
            val dom7 = scale.minor[6]
            noteName = if (dom7.contains("#")) dom7[0].toString() else  dom7 + "b"
        } else if (interval == 9){
            // fourth note: 6
            noteName = scale.major[5]
        } else if (interval == 10){
            // fourth note: dom7
            noteName = scale.minor[6]
        } else if (interval == 11) {
            // fourth note: maj7
            noteName = scale.major[6]
        } else if (interval == 13){
            // fifth note: b9
            val ninth = scale.major[1]
            noteName = if (ninth.contains("#")) ninth[0].toString() else ninth + "b"
        } else if (interval == 14){
            // fifth note: 9
            noteName = scale.major[1]
        } else if (interval == 15){
            // fifth note: #9
            val ninth = scale.major[1]
            noteName = if (ninth.contains("b")) ninth[0].toString() else "$ninth#"
        } else if (interval == 17){
            // sixth note: 11
            noteName = scale.major[3]
        } else if (interval == 18){
            // sixth note: #11
            val eleventh = scale.major[3]
            noteName = if (eleventh.contains("b")) eleventh[0].toString() else "$eleventh#"
        } else if (interval == 21){
            // seventh note: 13
            noteName = scale.major[5]
        } else {
            noteName = scale.major[0]
        }

        val noteAcc: Accidental = if (noteName.contains("##")){
            Accidental.DOUBLESHARP
        } else if (noteName.contains("#")){
            Accidental.SHARP
        } else if (noteName.contains("bb")){
            Accidental.DOUBLEFLAT
        } else if (noteName.contains("b")){
            Accidental.FLAT
        } else {
            Accidental.NONE
        }

        val baseMk = (solution.root + interval) % 12

        return SheetNote(name = noteName, acc = noteAcc, baseMk = baseMk)
    }

    private fun getSheetNotes(
        roots: List<Root>,
        scales: List<Scale>,
        solution: ChordSolution,
        sheetDimensions: SheetDimensions
    ): List<SheetNote> {



        val notesList = mutableListOf<SheetNote>()
        val sheetNotesList = mutableListOf<SheetNote>()

        val baseOc = floor(solution.finalMidiKeys[0].toDouble() / 12.0).toInt()

        val chordIsMajor = solution.chord.isMajor
        val root = roots[solution.root]
        val chordAccsAreSharp = if (chordIsMajor) root.accMajor == "sharp" else root.accMinor == "sharp"
        val scale = scales.find { it.root == if (chordIsMajor) root.rootMajor else root.rootMinor }
            ?: return emptyList()


        //val intervalsShiftedByRoot = solution.chord.intervals.map { it + solution.root }

        // find correct notes for this chord and key
        for (interval in solution.chord.intervals){
            val note = getNote(interval, scale, solution)
            notesList.add(note)
        }

        // bring notes in correct order
        for (mk in solution.finalMidiKeys){
            val note = notesList.find { it.baseMk == mk % 12 }
            if (note != null){
                sheetNotesList.add(note)
            }
        }

        // map actual intervals to notes (for transY)
        var dxFactor = 0
        var prevLineIndex = 1000

        for ((i, sheetNote) in sheetNotesList.withIndex()){

            val baseLineIndex = listOf("C", "D", "E", "F", "G", "A", "B").indexOf(sheetNotesList[i].name[0].toString())+2
            // transpose chord so it's at lowest possible position on sheet
            val lowestMkInChord = listOf(48, 60, 72, 84, 96).find { it > solution.finalMidiKeys[0] } ?: 60
            var ocShift = floor((solution.finalMidiKeys[i]-lowestMkInChord+12).toFloat() / 12).toInt()
            if (sheetNote.name.startsWith("Cb")){
                ocShift += 1
            } else if (sheetNote.name.startsWith("B#")){
                ocShift -= 1
            }
            val lineIndex = baseLineIndex + ocShift * 7
            dxFactor = if (lineIndex - prevLineIndex == 1 && dxFactor == 0) 1 else 0
            prevLineIndex = lineIndex

            sheetNote.transX = sheetDimensions.noteTransX + dxFactor * sheetDimensions.noteDx
            sheetNote.transY = sheetDimensions.lowestNoteY - lineIndex * sheetDimensions.linesDistance
            sheetNote.lineIndex = lineIndex
            sheetNote.accDx = sheetDimensions.noteTransX

            // ledger lines top
            val ledgerLinesNumTop = (floor(lineIndex.toFloat() / 2f) - 6f).toInt()
            if (ledgerLinesNumTop > 0){
                for (index in 1..ledgerLinesNumTop){
                    val newLedgerLine = LedgerLine(
                        sheetDimensions.noteTransX + dxFactor * sheetDimensions.noteDx - sheetDimensions.ledgerLineDx,
                        sheetDimensions.lowestLine - 2*(6 + index) * sheetDimensions.linesDistance// - i * 2 * sheetDimensions.linesDistance
                    )
                    if (!ledgerLinesTop.contains(newLedgerLine)){
                        if ((dxFactor > 0 && index == ledgerLinesNumTop) || dxFactor == 0){
                            ledgerLinesTop.add(newLedgerLine)
                        }
                    }
                }
            }

            // ledger lines bottom
            val ledgerLinesNumBottom = ceil((3 - lineIndex).toFloat() / 2).toInt()
            if (ledgerLinesNumBottom > 0){
                for (index in 1..ledgerLinesNumBottom){
                    val newLedgerLine = LedgerLine(
                        sheetDimensions.noteTransX + dxFactor * sheetDimensions.noteDx - sheetDimensions.ledgerLineDx,
                        sheetDimensions.lowestLine - 2 * (2-index) * sheetDimensions.linesDistance
                    )
                    if (!ledgerLinesBottom.contains(newLedgerLine)){
                        if ((dxFactor > 0 && index == ledgerLinesNumBottom) || dxFactor == 0){
                            ledgerLinesBottom.add(newLedgerLine)
                        }
                    }
                }
            }
        }

        // accidentals dx
        val accSheetNotes = sheetNotesList.filter { it.acc != Accidental.NONE }
        var currAccXIndex = 2
        var currAccLine = 0
        var prevAccLine = -1000
        for (sheetNote in accSheetNotes.reversed()){

            currAccLine = sheetNote.lineIndex
            currAccXIndex = if (prevAccLine - currAccLine  > 3){
                0
            } else {
                if (currAccXIndex+1 > 2) 0 else currAccXIndex+1
            }
            prevAccLine = currAccLine

            sheetNote.accDx -= sheetDimensions.accDxs[currAccXIndex]
        }
        return sheetNotesList
    }
}


@Composable
fun MusicSheet(roots: List<Root>, scales: List<Scale>, solution: ChordSolution, gameStatus: GameStatus) {

    val note = ImageVector.vectorResource(id = R.drawable.note)
    val accSharp = ImageVector.vectorResource(id = R.drawable.accsharp)
    val accFlat = ImageVector.vectorResource(id = R.drawable.accflat)
    val accNatural = ImageVector.vectorResource(R.drawable.clef)
    val accDoubleSharp = ImageVector.vectorResource(R.drawable.acc_double_sharp)
    val accDoubleFlat = ImageVector.vectorResource(R.drawable.acc_double_flat)


    val boxWInDp = 140.dp

    val dens = LocalDensity.current.density
    val sheetDimensions = SheetDimensions(dens)
    val sheet by remember { mutableStateOf(Sheet(roots, scales, solution, sheetDimensions)) }
    sheet.updateSheetNotes(roots, scales, solution, sheetDimensions)

    Box(
        modifier = Modifier
            .width(boxWInDp)
            .height(100.dp)
            //.background(Color(0x22777777))
    ) {

        /*Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.sheet_bg_clef),
            contentDescription = "",
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier.matchParentSize()
        )*/

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.clef),
            contentDescription = "",
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier.width(27.dp).graphicsLayer(translationX = sheetDimensions.clefDx, translationY = sheetDimensions.clefDy)
        )

        // LINES
        for (dy in sheet.linesDy) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.line_note),
                contentDescription = "",
                tint = MaterialTheme.colors.onSecondary,
                modifier = Modifier
                    .width(boxWInDp)
                    .graphicsLayer(translationY = dy)
            )
        }

        if (gameStatus == GameStatus.FOUND){
            for (line in sheet.ledgerLinesBottom){
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.leger_line),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .width(22.dp)
                        .graphicsLayer(
                            translationX = line.dx,
                            translationY = line.dy,
                        )
                )
            }

            for (line in sheet.ledgerLinesTop) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.leger_line),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .width(22.dp)
                        .graphicsLayer(
                            translationX = line.dx,
                            translationY = line.dy
                        )
                )
            }

            // NOTES

            for (sheetNote in sheet.sheetNotes){
                Icon(
                    imageVector = note,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .width(17.dp)
                        .graphicsLayer(
                            translationX = sheetNote.transX,
                            translationY = sheetNote.transY
                        ))
                if (sheetNote.acc != Accidental.NONE){
                    val imageVector = when(sheetNote.acc){
                        Accidental.SHARP -> accSharp
                        Accidental.FLAT -> accFlat
                        Accidental.DOUBLESHARP -> accDoubleSharp
                        Accidental.DOUBLEFLAT -> accDoubleFlat
                        else -> accNatural
                    }
                    val width = when(sheetNote.acc){
                        Accidental.SHARP -> 6.dp
                        Accidental.FLAT -> 7.dp
                        Accidental.DOUBLESHARP -> 8.dp
                        Accidental.DOUBLEFLAT -> 13.dp
                        else -> 0.dp
                    }
                    val dy = if (sheetNote.acc == Accidental.DOUBLESHARP) sheetNote.transY else sheetNote.transY - sheetDimensions.accDy
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .width(width)
                            .graphicsLayer(
                                translationX = sheetNote.accDx,
                                translationY = dy
                            ))
                }
            }
        }
    }
}

/*
data class SheetNote(
    var transX: Float,
    val transY: Float,
    var hasAcc: Boolean,
    val isSharp: Boolean,
    var isNatural: Boolean,
    var accX: Float,
    val lineIndex: Int
)
 */

/*
private fun getSheetNotes(
        midiKeys: List<Int>,
        roots: List<Root>,
        solution: ChordSolution,
        sheetDimensions: SheetDimensions) : List<SheetNote>{

        val baseOc = floor(midiKeys[0].toDouble() / 12.0).toInt()
        val zeroed = midiKeys.map { it - baseOc * 12 }

        val sheetNotesList = mutableListOf<SheetNote>()

        var prevLineIndex = -1000
        var currDxFactor = 0

        val root = roots[solution.root]
        Log.e("", "${root.sharpFlatRoot} ${solution.chord.name}")
        val accsAreSharp = if (solution.chord.isMajor) root.accMajor == "sharp" else root.accMinor == "sharp"

        for (zeroedMk in zeroed){

            var hasAcc = false

            val note = roots[zeroedMk % 12]
            val octave = floor(zeroedMk.toFloat() / 12f).toInt() * 12 /* TODO: BUG */

            val whiteRoots = roots.filter { it.displayName.length == 1 }.map { it.displayName }

            var lineIndex = whiteRoots.indices.find { whiteRoots[it] == if (hasAcc && !accsAreSharp) note.displayName[2].toString() else note.displayName[0].toString() }
            if (lineIndex != null){
                lineIndex += octave

                if (lineIndex - prevLineIndex == 1 && currDxFactor == 0){
                    currDxFactor = 1
                } else if (lineIndex == prevLineIndex){
                    currDxFactor = 3
                } else {
                    currDxFactor = 0
                }

                if (note.displayName.length > 1){
                    hasAcc = true
                }

                prevLineIndex = lineIndex

                val transX = sheetDimensions.noteTransX + currDxFactor * sheetDimensions.noteDx
                val transY = sheetDimensions.noteCY - lineIndex * sheetDimensions.linesDistance

                val extraLineIndex = (floor(lineIndex.toFloat() / 2f) - 5f).toInt()
                if (extraLineIndex > 0){
                    for (i in 1..extraLineIndex){
                        val newExtraLine = ExtraLine(
                            sheetDimensions.noteTransX + currDxFactor * sheetDimensions.noteDx - sheetDimensions.extraLineDx,
                            sheetDimensions.lineUpperY - i * 2 * sheetDimensions.linesDistance
                        )
                        if (!extraLinesTop.contains(newExtraLine)){
                            if ((currDxFactor > 0 && i == extraLineIndex) || currDxFactor == 0){
                                extraLinesTop.add(newExtraLine)
                            }
                        }
                    }
                }

                if (lineIndex == 0){
                    extraLinesBottom.add(ExtraLine(transX - sheetDimensions.extraLineDx, sheetDimensions.lineUpperY + 10 * sheetDimensions.linesDistance))
                }
                val accX = sheetDimensions.noteTransX

                sheetNotesList.add(SheetNote(transX, transY, hasAcc, accsAreSharp, false, accX, lineIndex))
            }
        }

        val accSheetNotes = sheetNotesList.filter { it.hasAcc }
        var currAccXIndex = 2
        var currAccLine = 0
        var prevAccLine = -1000
        for (sheetNote in accSheetNotes.reversed()){

            currAccLine = sheetNote.lineIndex
            currAccXIndex = if (prevAccLine - currAccLine  > 3){
                0
            } else {
                if (currAccXIndex+1 > 2) 0 else currAccXIndex+1
            }
            prevAccLine = currAccLine

            sheetNote.accX -= sheetDimensions.accDx[currAccXIndex]
        }

        for ((i, sheetNote) in sheetNotesList.withIndex()){
            if (i > 0){
                val prevSheetNote = sheetNotesList[i-i]
                if (sheetNote.lineIndex == prevSheetNote.lineIndex){
                    if (prevSheetNote.hasAcc){
                        sheetNote.hasAcc = true
                        sheetNote.isNatural = true
                        sheetNote.accX += 3 * sheetDimensions.noteDx
                    }
                }
            }
        }

        return sheetNotesList
    }
 */