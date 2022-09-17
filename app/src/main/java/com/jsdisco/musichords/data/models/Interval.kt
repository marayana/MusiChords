package com.jsdisco.musichords.data.models

enum class Quality {PERFECT, MAJOR, MINOR, AUG}

data class Interval(
    val halfTones: Int,
    val stringIndex: Int,
    val quality: Quality
)
