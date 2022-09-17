package com.jsdisco.musichords.domain.models

import com.jsdisco.musichords.data.models.Interval

data class IntervalSolution(
    val interval: Interval,
    val isCompound: Boolean,
    val midiKeys: List<Int>
)