package com.jsdisco.soundscompose.domain.models

import com.jsdisco.soundscompose.data.models.Chord

data class ChordSolution(
    val rootName: String,
    val displayName: String,
    val chord: Chord,
    val root: Int, // (0..11)
    val finalMidiKeys: List<Int>,
    val inversions: Int,
    val openPositionIndex: Int,
    var octaveTranspose: Int = 0
)
