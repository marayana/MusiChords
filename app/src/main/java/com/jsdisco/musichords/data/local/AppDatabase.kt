package com.jsdisco.musichords.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsdisco.musichords.data.local.models.SettingsChords
import com.jsdisco.musichords.data.local.models.ChordsSet
import com.jsdisco.musichords.data.local.models.SettingsRelativePitch

@Database(
    entities = [
        SettingsChords::class,
        SettingsRelativePitch::class,
        ChordsSet::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val appDao: AppDao
}