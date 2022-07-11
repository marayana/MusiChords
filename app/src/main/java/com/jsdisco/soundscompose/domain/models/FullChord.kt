package com.jsdisco.soundscompose.domain.models

data class FullChord(
    val root: String,
    val chordName: String,
    val intervals: List<Int>,
    val midiKeys: List<Int>,
    val noteNames: List<String>
)