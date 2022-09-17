package com.jsdisco.musichords.presentation.relativepitch.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.data.models.Quality
import com.jsdisco.musichords.data.models.Root
import com.jsdisco.musichords.domain.models.IntervalSolution
import com.jsdisco.musichords.presentation.chords.GameStatus
import kotlin.math.ceil
import kotlin.math.floor

enum class Accidental { NONE, SHARP, FLAT }

data class SheetNote(
    val name: String,
    var lineIndex: Int = 0,
    var transX: Float = 0f,
    var transY: Float = 0f,
    var acc: Accidental,
    var accDx: Float = 0f
)

class SheetDimensions(dens: Float) {
    val noteTransX = 85f * dens // base shift to the right
    val noteDx =
        15f * dens // shifting further to the right for notes that are on the immediate next line

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
    roots: List<Root>,
    solution: IntervalSolution,
    private val sheetDimensions: SheetDimensions
) {

    val linesDy = mutableListOf<Float>()
    val ledgerLinesTop = mutableListOf<LedgerLine>()
    val ledgerLinesBottom = mutableListOf<LedgerLine>()

    var sheetNotes by mutableStateOf(getSheetNotes(roots, solution, sheetDimensions))

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
        solution: IntervalSolution,
        sheetDimensions: SheetDimensions
    ) {
        ledgerLinesTop.clear()
        ledgerLinesBottom.clear()
        sheetNotes = getSheetNotes(roots, solution, sheetDimensions)
    }

    private fun getFirstNote(roots: List<Root>, solution: IntervalSolution): SheetNote {
        val note = roots[solution.midiKeys[0] % 12]
        val noteName = if (note.displayName.length > 1) {
            if (solution.interval.halfTones == 1 || solution.interval.halfTones == 2) {
                note.displayName.slice(0..1)
            } else {
                listOf(note.displayName.slice(0..1), note.displayName.slice(3..4)).shuffled().last()
            }
        } else {
            note.displayName
        }

        val noteAcc: Accidental = if (noteName.contains("#")) {
            Accidental.SHARP
        } else if (noteName.contains("b")) {
            Accidental.FLAT
        } else {
            Accidental.NONE
        }

        return SheetNote(name = noteName, acc = noteAcc)
    }

    private fun getSecondNote(
        firstNote: SheetNote,
        roots: List<Root>,
        solution: IntervalSolution
    ): SheetNote {

        val note = roots[(solution.midiKeys[0] + solution.interval.halfTones) % 12]
        val noteName: String

        if (note.displayName.length > 1) {
            noteName = when (solution.interval.quality) {
                Quality.MAJOR, Quality.AUG -> {
                    note.displayName.slice(0..1)
                }
                Quality.MINOR -> {
                    note.displayName.slice(3..4)
                }
                Quality.PERFECT -> {
                    if (firstNote.name.contains("b")) {
                        note.displayName.slice(3..4)
                    } else {
                        note.displayName.slice(0..1)
                    }
                }
            }
        } else {
            noteName = note.displayName
        }

        val noteAcc: Accidental = if (noteName.contains("#")) {
            Accidental.SHARP
        } else if (noteName.contains("b")) {
            Accidental.FLAT
        } else {
            Accidental.NONE
        }

        return SheetNote(name = noteName, acc = noteAcc)
    }

    private fun getSheetNotes(
        roots: List<Root>,
        solution: IntervalSolution,
        sheetDimensions: SheetDimensions
    ): List<SheetNote> {

        val sheetNotesList = mutableListOf<SheetNote>()

        val firstNote = getFirstNote(roots, solution)
        val secondNote = getSecondNote(firstNote, roots, solution)

        sheetNotesList.add(firstNote)
        sheetNotesList.add(secondNote)


        // map actual intervals to notes (for transY)
        var dxFactor = 0
        var prevLineIndex = 1000

        for ((i, sheetNote) in sheetNotesList.withIndex()) {

            val baseLineIndex = listOf(
                "C",
                "D",
                "E",
                "F",
                "G",
                "A",
                "B"
            ).indexOf(sheetNotesList[i].name[0].toString()) + 2

            // transpose notes so it's at lowest possible position on sheet
            val lowestMk = listOf(48, 60, 72, 84, 96).find { it > solution.midiKeys[0] } ?: 60
            val ocShift = floor((solution.midiKeys[i] - lowestMk + 12).toFloat() / 12).toInt()

            val lineIndex = baseLineIndex + ocShift * 7
            dxFactor = if (lineIndex - prevLineIndex == 1 && dxFactor == 0) 1 else 0
            prevLineIndex = lineIndex

            sheetNote.transX = sheetDimensions.noteTransX + dxFactor * sheetDimensions.noteDx
            sheetNote.transY =
                sheetDimensions.lowestNoteY - lineIndex * sheetDimensions.linesDistance
            sheetNote.lineIndex = lineIndex
            sheetNote.accDx = sheetDimensions.noteTransX

            // ledger lines top
            val ledgerLinesNumTop = (floor(lineIndex.toFloat() / 2f) - 6f).toInt()
            if (ledgerLinesNumTop > 0) {
                for (index in 1..ledgerLinesNumTop) {
                    val newLedgerLine = LedgerLine(
                        sheetDimensions.noteTransX + dxFactor * sheetDimensions.noteDx - sheetDimensions.ledgerLineDx,
                        sheetDimensions.lowestLine - 2 * (6 + index) * sheetDimensions.linesDistance
                    )
                    if (!ledgerLinesTop.contains(newLedgerLine)) {
                        if ((dxFactor > 0 && index == ledgerLinesNumTop) || dxFactor == 0) {
                            ledgerLinesTop.add(newLedgerLine)
                        }
                    }
                }
            }

            // ledger lines bottom
            val ledgerLinesNumBottom = ceil((3 - lineIndex).toFloat() / 2).toInt()
            if (ledgerLinesNumBottom > 0) {
                for (index in 1..ledgerLinesNumBottom) {
                    val newLedgerLine = LedgerLine(
                        sheetDimensions.noteTransX + dxFactor * sheetDimensions.noteDx - sheetDimensions.ledgerLineDx,
                        sheetDimensions.lowestLine - 2 * (2 - index) * sheetDimensions.linesDistance
                    )
                    if (!ledgerLinesBottom.contains(newLedgerLine)) {
                        if ((dxFactor > 0 && index == ledgerLinesNumBottom) || dxFactor == 0) {
                            ledgerLinesBottom.add(newLedgerLine)
                        }
                    }
                }
            }
        }

        // accidentals dx
        val accSheetNotes = sheetNotesList.filter { it.acc != Accidental.NONE }
        var currAccXIndex = 2
        var currAccLine: Int
        var prevAccLine = -1000
        for (sheetNote in accSheetNotes.reversed()) {

            currAccLine = sheetNote.lineIndex
            currAccXIndex = if (prevAccLine - currAccLine > 3) {
                0
            } else {
                if (currAccXIndex + 1 > 2) 0 else currAccXIndex + 1
            }
            prevAccLine = currAccLine

            sheetNote.accDx -= sheetDimensions.accDxs[currAccXIndex]
        }
        return sheetNotesList
    }
}


@Composable
fun MusicSheetRP(roots: List<Root>, solution: IntervalSolution, gameStatus: GameStatus) {

    val note = ImageVector.vectorResource(id = R.drawable.note)
    val accSharp = ImageVector.vectorResource(id = R.drawable.accsharp)
    val accFlat = ImageVector.vectorResource(id = R.drawable.accflat)
    val clef = ImageVector.vectorResource(R.drawable.clef)


    val boxWInDp = 140.dp

    val dens = LocalDensity.current.density
    val sheetDimensions = SheetDimensions(dens)
    val sheet by remember { mutableStateOf(Sheet(roots, solution, sheetDimensions)) }
    sheet.updateSheetNotes(roots, solution, sheetDimensions)

    Box(
        modifier = Modifier
            .width(boxWInDp)
            .height(100.dp)
    ) {

        Icon(
            imageVector = clef,
            contentDescription = "",
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .width(27.dp)
                .graphicsLayer(
                    translationX = sheetDimensions.clefDx,
                    translationY = sheetDimensions.clefDy
                )
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

        if (gameStatus == GameStatus.FOUND) {
            for (line in sheet.ledgerLinesBottom) {
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
            for (sheetNote in sheet.sheetNotes) {
                Icon(
                    imageVector = note,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .width(17.dp)
                        .graphicsLayer(
                            translationX = sheetNote.transX,
                            translationY = sheetNote.transY
                        )
                )
                if (sheetNote.acc != Accidental.NONE) {
                    val imageVector = when (sheetNote.acc) {
                        Accidental.SHARP -> accSharp
                        else -> accFlat
                    }
                    val width = when (sheetNote.acc) {
                        Accidental.SHARP -> 6.dp
                        else -> 7.dp

                    }
                    val dy = sheetNote.transY - sheetDimensions.accDy
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .width(width)
                            .graphicsLayer(
                                translationX = sheetNote.accDx,
                                translationY = dy
                            )
                    )
                }
            }
        }
    }
}
