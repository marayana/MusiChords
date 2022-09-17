package com.jsdisco.musichords.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsRelativePitch(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var playIntervalOnTap: Boolean = true,
    var withCompoundIntervals: Boolean = true,
    var isNotationAbbr: Boolean = false,
    var interval1: Boolean = true,
    var interval2: Boolean = true,
    var interval3: Boolean = true,
    var interval4: Boolean = true,
    var interval5: Boolean = true,
    var interval6: Boolean = true,
    var interval7: Boolean = true,
    var interval8: Boolean = true,
    var interval9: Boolean = true,
    var interval10: Boolean = true,
    var interval11: Boolean = true,
    var interval12: Boolean = true
)