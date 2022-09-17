package com.jsdisco.musichords.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChordsSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isCustom: Boolean = false,
    val name: String,
    val stringIndex: Int = 0,
    val c1: String? = null,
    val c2: String? = null,
    val c3: String? = null,
    val c4: String? = null,
    val c5: String? = null,
    val c6: String? = null,
    val c7: String? = null,
    val c8: String? = null,
    val c9: String? = null,
    val c10: String? = null,
    val c11: String? = null,
    val c12: String? = null
)