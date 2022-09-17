package com.jsdisco.musichords.data.models

data class Chord(
    val name: String,
    val stringIndex: Int,
    val intervals: List<Int>,
    val isMajor: Boolean
)
