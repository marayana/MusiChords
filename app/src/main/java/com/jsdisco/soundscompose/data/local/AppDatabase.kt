package com.jsdisco.soundscompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsdisco.soundscompose.data.local.models.SettingsChords
import com.jsdisco.soundscompose.data.local.models.ChordsSet

@Database(
    entities = [
        SettingsChords::class,
        ChordsSet::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val appDao: AppDao
}