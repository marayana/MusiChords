package com.jsdisco.musichords.data

import com.jsdisco.musichords.data.local.AppDatabase
import com.jsdisco.musichords.data.local.models.SettingsRelativePitch
import com.jsdisco.musichords.data.models.Interval
import com.jsdisco.musichords.data.models.Quality
import com.jsdisco.musichords.data.models.Root

class RelativePitchRepository(private val database: AppDatabase) {

    suspend fun initSettingsRP(){
        if (database.appDao.getSettingsRPCount() == 0){
            val settingsRP = SettingsRelativePitch()
            database.appDao.insertSettingsRP(settingsRP)
        }
    }

    suspend fun getSettingsRP() : SettingsRelativePitch{
        return database.appDao.getSettingsRP()
    }

    suspend fun updateSettingsRP(setting: SettingsRelativePitch){
        database.appDao.updateSettingsRP(setting)
    }

    val intervals: List<Interval> = listOf(
        Interval(1, 0, Quality.MINOR),
        Interval(2, 1, Quality.MAJOR),
        Interval(3, 2, Quality.MINOR),
        Interval(4, 3, Quality.MAJOR),
        Interval(5, 4, Quality.PERFECT),
        Interval(6, 5, Quality.AUG),
        Interval(7, 6, Quality.PERFECT),
        Interval(8, 7, Quality.MINOR),
        Interval(9, 8, Quality.MAJOR),
        Interval(10, 9, Quality.MINOR),
        Interval(11, 10, Quality.MAJOR),
        Interval(12, 11, Quality.PERFECT)
    )

    // for music sheet
    val roots = listOf(
        Root( "C", "C", "C", "sharp", "flat"),
        Root( "C#/Db", "Db", "C#", "flat", "sharp"),
        Root( "D","D", "D", "sharp", "flat"),
        Root( "D#/Eb", "Eb", "Eb", "flat", "flat"),
        Root( "E", "E", "E", "sharp", "sharp"),
        Root( "F", "F", "F", "flat", "flat"),
        Root( "F#/Gb", "Gb", "F#", "flat", "sharp"),
        Root( "G", "G", "G", "sharp", "sharp"),
        Root( "G#/Ab", "Ab", "G#", "flat", "sharp"),
        Root( "A", "A", "A", "sharp", "flat"),
        Root( "A#/Bb", "Bb", "Bb", "flat", "flat" ),
        Root("B", "B", "B", "sharp", "sharp"),
    )
}