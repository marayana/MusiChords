package com.jsdisco.soundscompose.data.local

import androidx.room.*
import com.jsdisco.soundscompose.data.local.models.SettingsChords
import com.jsdisco.soundscompose.data.local.models.ChordsSet

@Dao
interface AppDao {

    // SettingsChords

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettingsChords(setting: SettingsChords)

    @Update
    suspend fun updateSettingsChords(setting: SettingsChords)

    @Query("SELECT COUNT(*) FROM SettingsChords")
    fun getSettingsChordsCount(): Int

    @Query("SELECT * FROM SettingsChords")
    suspend fun getSettingsChords() : SettingsChords

    @Query("SELECT activeChordSetId FROM SettingsChords")
    suspend fun getActiveChordSetId() : Long

    // ChordsSet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChordsSet(set: ChordsSet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChordsSets(sets: List<ChordsSet>)

    @Query("SELECT COUNT(*) FROM ChordsSet")
    fun getChordsSetsCount() : Int

    @Query("SELECT * FROM ChordsSet")
    suspend fun getChordsSets() : List<ChordsSet>

    @Query("SELECT id FROM ChordsSet")
    fun getChordsSetsIds() : List<Long>

    @Query("SELECT * FROM ChordsSet WHERE id = :id")
    suspend fun getChordsSetById(id: Long) : ChordsSet

    @Delete
    suspend fun deleteChordsSet(set: ChordsSet)

}