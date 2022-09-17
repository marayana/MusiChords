package com.jsdisco.musichords.domain.models

data class ChordSearchResult(
    val root: String,
    val chordName: String,
    val intervals: List<Int>,
    val midiKeys: List<Int>,
    val midiKeysPiano: List<Int>,
    val noteNames: List<String>,
    val noteStringIndices: List<Int>
)