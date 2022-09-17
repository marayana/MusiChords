package com.jsdisco.musichords.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsChords(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var activeChordSetId: Long = 0,
    var playChordOnTap: Boolean = true,
    var withInversions: Boolean = true,
    var withOpenPosition: Boolean = false
)