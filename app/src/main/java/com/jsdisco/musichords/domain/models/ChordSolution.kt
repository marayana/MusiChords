package com.jsdisco.musichords.domain.models

import com.jsdisco.musichords.data.models.Chord

data class ChordSolution(
    val rootName: String,
    val rootStringIndex: Int,
    val chordStringIndex: Int,
    val chord: Chord,
    val root: Int, // (0..11)
    val finalMidiKeys: List<Int>,
    val inversions: Int,
    val openPositionIndex: Int,
    var octaveTranspose: Int = 0
)
