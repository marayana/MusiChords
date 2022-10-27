package com.jsdisco.musichords.data

import com.jsdisco.musichords.data.local.AppDatabase
import com.jsdisco.musichords.data.local.models.SettingsChords
import com.jsdisco.musichords.data.local.models.ChordsSet
import com.jsdisco.musichords.data.models.Chord
import com.jsdisco.musichords.data.models.Note
import com.jsdisco.musichords.data.models.Root
import com.jsdisco.musichords.data.models.Scale

class ChordsRepository(private val database: AppDatabase) {

    suspend fun initSettingsChords(){

        if(database.appDao.getSettingsChordsCount() == 0){
            val chordSetId = database.appDao.getChordsSetsIds()[0]
            val newSetting = SettingsChords(activeChordSetId = chordSetId)
            database.appDao.insertSettingsChords(newSetting)
        }
    }

    suspend fun initChordsSets(){
        if (database.appDao.getChordsSetsCount() == 0){

            val settingsChordsList = mutableListOf<ChordsSet>()

            chordsDefault.forEachIndexed { index, chordsList ->
                val chordNames = chordsList.map{it.name}
                settingsChordsList.add(ChordsSet(
                    stringIndex = index,
                    name = chordNames[index],
                    c1 = if (chordNames.size > 0) chordNames[0] else null,
                    c2 = if (chordNames.size > 1) chordNames[1] else null,
                    c3 = if (chordNames.size > 2) chordNames[2] else null,
                    c4 = if (chordNames.size > 3) chordNames[3] else null,
                    c5 = if (chordNames.size > 4) chordNames[4] else null,
                    c6 = if (chordNames.size > 5) chordNames[5] else null,
                    c7 = if (chordNames.size > 6) chordNames[6] else null,
                    c8 = if (chordNames.size > 7) chordNames[7] else null,
                    c9 = if (chordNames.size > 8) chordNames[8] else null,
                    c10 = if (chordNames.size > 9) chordNames[9] else null,
                    c11 = if (chordNames.size > 10) chordNames[10] else null,
                    c12 = if (chordNames.size > 11) chordNames[11] else null,
                ))
            }

            database.appDao.insertChordsSets(settingsChordsList)
        }
    }

    suspend fun getSettingsChords() : SettingsChords {
        return database.appDao.getSettingsChords()
    }

    suspend fun updateSettingsChords(setting: SettingsChords){
        database.appDao.updateSettingsChords(setting)
    }

    suspend fun getChordsSets() : List<ChordsSet>{
        return database.appDao.getChordsSets()
    }

    suspend fun addChordsSet(set: ChordsSet){
        database.appDao.insertChordsSet(set)
    }

    suspend fun getActiveChordsSet() : ChordsSet {
        val activeId = database.appDao.getActiveChordSetId()
        return database.appDao.getChordsSetById(activeId)
    }

    suspend fun deleteChordsSet(set: ChordsSet){
        database.appDao.deleteChordsSet(set)
    }


    fun getActiveChords(activeChordsSet: ChordsSet) : List<Chord>{
        val chord1 = activeChordsSet.c1
        val chord2 = activeChordsSet.c2
        val chord3 = activeChordsSet.c3
        val chord4 = activeChordsSet.c4
        val chord5 = activeChordsSet.c5
        val chord6 = activeChordsSet.c6
        val chord7 = activeChordsSet.c7
        val chord8 = activeChordsSet.c8
        val chord9 = activeChordsSet.c9
        val chord10 = activeChordsSet.c10
        val chord11 = activeChordsSet.c11
        val chord12 = activeChordsSet.c12
        val chordsActiveList = listOf(chord1, chord2, chord3, chord4, chord5, chord6, chord7, chord8, chord9, chord10, chord11, chord12)
        val chordsList = mutableListOf<Chord>()
        for(c in chordsActiveList){
            if (c != null){
                val chord = chords.find { it.name == c }
                if (chord != null){
                    chordsList.add(chord)
                }
            }
        }
        return chordsList
    }

    val scales = listOf(
        Scale("C", listOf("C", "D", "E", "F", "G", "A", "B"), listOf("C", "D", "Eb", "F", "G", "Ab", "Bb"), "sharp", "flat"),
        Scale("C#", listOf("C#", "D#", "E#", "F#", "G#", "A#", "B#"), listOf("C#", "D#", "E", "F#", "G#", "A", "B"), "sharp", "sharp"),
        Scale("Db", listOf("Db", "Eb", "F", "Gb", "Ab", "Bb", "C"), listOf("Db", "Eb", "Fb", "Gb", "Ab", "Bbb", "Cb"), "flat", "flat"),
        Scale("D", listOf("D", "E", "F#", "G", "A", "B", "C#"), listOf("D", "E", "F", "G", "A", "Bb", "C"), "sharp", "flat"),
        Scale("D#", listOf("D#", "E#", "F##", "G#", "A#", "B#", "C##"), listOf("D#", "E#", "F#", "G#", "A#", "B", "C#"), "sharp", "sharp"),
        Scale("Eb", listOf("Eb", "F","G", "Ab", "Bb", "C", "D" ), listOf("Eb", "F", "Gb", "Ab", "Bb", "Cb", "Db"), "flat", "flat"),
        Scale("E", listOf("E", "F#", "G#", "A", "B", "C#", "D#"), listOf("E", "F#", "G", "A", "B", "C", "D"), "sharp", "sharp"),
        Scale("F", listOf("F", "G", "A", "Bb", "C", "D", "E"), listOf("F", "G", "Ab", "Bb", "C", "Db", "Eb"), "flat", "flat"),
        Scale("F#", listOf("F#", "G#", "A#", "B", "C#", "D#", "E#"), listOf("F#", "G#", "A", "B", "C#", "D", "E"), "sharp", "sharp"),
        Scale("Gb", listOf("Gb", "Ab", "Bb", "Cb", "Db", "Eb", "F"), listOf("Gb", "Ab", "Bbb", "Cb", "Db", "Ebb", "Fb"), "flat", "flat"),
        Scale("G", listOf("G", "A", "B", "C", "D", "E", "F#"), listOf("G", "A", "Bb", "C", "D", "Eb", "F"), "sharp", "flat"),
        Scale("G#", listOf("G#", "A#", "B#", "C#", "D#", "E#", "F##"), listOf("G#", "A#", "B", "C#", "D#", "E", "F#"), "sharp", "sharp"),
        Scale("Ab", listOf("Ab", "Bb", "C", "Db", "Eb", "F", "G"), listOf("Ab", "Bb", "Cb", "Db", "Eb", "Fb", "Gb"), "flat", "flat"),
        Scale("A", listOf("A", "B", "C#", "D", "E", "F#", "G#"), listOf("A", "B", "C", "D", "E", "F", "G"), "sharp", "flat"),
        Scale("A#", listOf("A#", "B#", "C##", "D#", "E#", "F##", "G##"), listOf("A#", "B#", "C#", "D#", "E#", "F#", "G#"), "sharp", "sharp"),
        Scale("Bb", listOf("Bb", "C", "D", "Eb", "F", "G", "A"), listOf("Bb", "C", "Db", "Eb", "F", "Gb", "Ab"), "flat", "flat"),
        Scale("B", listOf("B", "C#", "D#", "E", "F#", "G#", "A#"), listOf("B", "C#", "D", "E", "F#", "G", "A"), "sharp", "sharp")
    )

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

    // for chords / chords info -> note string index
    val notes = listOf(
        "C", "D", "E", "F", "G", "A", "B", "Cb", "Cbb", "C#", "C##", "Db", "Dbb", "D#", "D##", "Eb", "Ebb", "E#"
, "E##", "Fb", "Fbb", "F#", "F##", "Gb", "Gbb", "G#", "G##", "Ab", "Abb", "A#", "A##", "Bb", "Bbb", "B#", "B##"    )


    // chords info (piano)
    val notesWhite = listOf(
        Note(60, 3, "c", "C"),
        Note(62, 3, "d", "D"),
        Note(64, 3, "e", "E"),
        Note(65, 3, "f", "F"),
        Note(67, 3, "g", "G"),
        Note(69, 3, "a", "A"),
        Note(71, 3, "b", "B"),
        Note(72, 4, "c", "C"),
        Note(74, 4, "d", "D"),
        Note(76, 4, "e", "E"),
        Note(77, 4, "f", "F"),
        Note(79, 4, "g", "G"),
        Note(81, 4, "a", "A"),
        Note(83, 4, "b", "B"),
        Note(84, 5, "c", "C"),
        Note(86, 5, "d", "D"),
        Note(88, 5, "e", "E"),
        Note(89, 5, "f", "F"),
        Note(91, 5, "g", "G"),
        Note(93, 5, "a", "A"),
        Note(95, 5, "b", "B"),
        Note(96, 6, "c", "C"),
    )

    // chords info (piano)
    val notesBlack = listOf(
        Note(61, 3, "db", "C#/Db"),
        Note(63, 3, "eb", "D#/Eb"),
        Note(66, 3, "gb", "F#/Gb"),
        Note(68, 3, "ab", "G#/Ab"),
        Note(70, 3, "bb", "A#/Bb"),
        Note(73, 4, "db", "C#/Db"),
        Note(75, 4, "eb", "D#/Eb"),
        Note(78, 4, "gb", "F#/Gb"),
        Note(80, 4, "ab", "G#/Ab"),
        Note(82, 4, "bb", "A#/Bb"),
        Note(85, 5, "db", "C#/Db"),
        Note(87, 5, "eb", "D#/Eb"),
        Note(90, 5, "gb", "F#/Gb"),
        Note(92, 5, "ab", "G#/Ab"),
        Note(94, 5, "bb", "A#/Bb"),
    )


    // common
    private val chords1 = listOf(
        Chord("major", 0, listOf(0, 4, 7),true),
        Chord("m", 1, listOf(0, 3, 7),false),
        Chord("7", 12, listOf(0, 4, 7, 10),true),
        Chord("9", 13, listOf(0, 4, 7, 10, 14),true),
        Chord("maj7", 26, listOf(0, 4, 7, 11),true),
        Chord("mmaj7", 38, listOf(0, 3, 7, 11),false),
        Chord("dim", 2, listOf(0, 3, 6),true),
        Chord("dim7", 3, listOf(0, 3, 6, 9),true),
        Chord("sus2", 4, listOf(0, 2, 7),true),
        Chord("sus4", 5, listOf(0, 5, 7),true),
        Chord("7sus4", 6, listOf(0, 5, 7, 10),true),
        Chord("aug", 7, listOf(0, 4, 8),true),
    )

    // minor
    private val chords2 = listOf(
        Chord("m", 1, listOf(0, 3, 7), false),
        Chord("m6", 32, listOf(0, 3, 7, 9), false),
        Chord("m69", 33, listOf(0, 3, 7, 9, 14), false),
        Chord("m7", 34, listOf(0, 3, 7, 10), false),
        Chord("m7b5", 35, listOf(0, 3, 6, 10), false),
        Chord("m9", 36, listOf(0, 3, 7, 10, 14), false),
        Chord("m11", 37, listOf(0, 3, 7, 10, 14, 17), false),
        Chord("mmaj7", 38, listOf(0, 3, 7, 11), false),
        Chord("mmaj7b5", 39, listOf(0, 3, 6, 11), false),
        Chord("mmaj9", 40, listOf(0, 3, 7, 11, 14), false),
        Chord("mmaj11", 41, listOf(0, 3, 7, 11, 14, 17), false),
        Chord("madd9", 43, listOf(0, 3, 7, 14), false)
    )

    // 7
    private val chords3 = listOf(
        Chord("7", 12, listOf(0, 4, 7, 10), true),
        Chord("m7", 34, listOf(0, 3, 7, 10), false),
        Chord("dim7", 3, listOf(0, 3, 6, 9), true),
        Chord("aug7", 8, listOf(0, 4, 8, 10), true),
        Chord("7sus4", 6, listOf(0, 5, 7, 10), true),
        Chord("maj7", 26, listOf(0, 4, 7, 11), true),
        Chord("mmaj7", 38, listOf(0, 3, 7, 11), false),
        Chord("7b5", 17, listOf(0, 4, 6, 10), true),
        Chord("m7b5", 35, listOf(0, 3, 6, 10), false),
    )

    // three notes
    private val chords4 = listOf(
        Chord("major", 0, listOf(0, 4, 7), true),
        Chord("m", 1, listOf(0, 3, 7), false),
        Chord("dim", 2, listOf(0, 3, 6), true),
        Chord("sus2", 4, listOf(0, 2, 7), true),
        Chord("sus4", 5, listOf(0, 5, 7), true),
        Chord("aug", 7, listOf(0, 4, 8), true)
    )

    // four notes
    private val chords5 = listOf(
        Chord("dim7", 3, listOf(0, 3, 6, 9),true),
        Chord("7sus4", 6, listOf(0, 5, 7, 10),true),
        Chord("aug7", 8, listOf(0, 4, 8, 10),true),
        Chord("6", 11, listOf(0, 4, 7, 9),true),
        Chord("7", 12, listOf(0, 4, 7, 10),true),
        Chord("7b5", 17, listOf(0, 4, 6, 10),true),
        Chord("maj7", 26, listOf(0, 4, 7, 11),true),
        Chord("m6", 32, listOf(0, 3, 7, 9),false),
        Chord("m7", 34, listOf(0, 3, 7, 10),false),
        Chord("mmaj7", 38, listOf(0, 3, 7, 11),false),
        Chord("add9", 42, listOf(0, 4, 7, 14),true),
        Chord("madd9", 43, listOf(0, 3, 7, 14),false),
    )

    // five notes
    private val chords6 = listOf(
        Chord("9", 13, listOf(0, 4, 7, 10, 14),true),
        Chord("m9", 36, listOf(0, 3, 7, 10, 14),false),
        Chord("69", 16, listOf(0, 4, 7, 9, 14),true),
        Chord("m69", 33, listOf(0, 3, 7, 9, 14),false),
        Chord("maj9", 27, listOf(0, 4, 7, 11, 14),true),
        Chord("mmaj9", 40, listOf(0, 3, 7, 11, 14),false),
        Chord("aug9", 9, listOf(0, 4, 8, 10, 14),true),
    )

    private val chordsDefault = listOf(chords1, chords2, chords3, chords4, chords5, chords6)


    val chords = listOf(

        Chord("major", 0, listOf(0, 4, 7),true),
        Chord("m", 1, listOf(0, 3, 7),false),
        Chord("dim", 2, listOf(0, 3, 6),true),
        Chord("dim7", 3, listOf(0, 3, 6, 9),true),
        Chord("sus2", 4, listOf(0, 2, 7),true),
        Chord("sus4", 5, listOf(0, 5, 7),true),
        Chord("7sus4", 6, listOf(0, 5, 7, 10),true),
        Chord("aug", 7, listOf(0, 4, 8),true),
        Chord("aug7", 8, listOf(0, 4, 8, 10),true),
        Chord("aug9", 9, listOf(0, 4, 8, 10, 14),true),
        Chord("5", 10, listOf(0, 7),true),
        Chord("6", 11, listOf(0, 4, 7, 9),true),
        Chord("7", 12, listOf(0, 4, 7, 10),true),
        Chord("9", 13, listOf(0, 4, 7, 10, 14),true),
        Chord("11", 14, listOf(0, 4, 7, 10, 14, 17),true),
        Chord("13", 15, listOf(0, 4, 7, 10, 14, 17, 21),true),
        Chord("69", 16, listOf(0, 4, 7, 9, 14),true),
        Chord("7b5", 17, listOf(0, 4, 6, 10),true),
        Chord("9b5", 18, listOf(0, 4, 6, 10, 14),true),
        Chord("7b9", 19, listOf(0, 4, 7, 10, 13),true),
        Chord("7sharp9", 20, listOf(0, 4, 7, 10, 15),true),
        Chord("7b5b9", 21, listOf(0, 4, 6, 10, 13),true),
        Chord("7sharp5b9", 22, listOf(0, 4, 8, 10, 13),true),
        Chord("7b5sharp9", 23, listOf(0, 4, 6, 10, 15),true),
        Chord("7sharp5sharp9", 24, listOf(0, 4, 8, 10, 15),true),
        Chord("9sharp11", 25, listOf(0, 4, 7, 10, 14, 18),true),
        Chord("maj7", 26, listOf(0, 4, 7, 11),true),
        Chord("maj9", 27, listOf(0, 4, 7, 11, 14),true),
        Chord("maj11", 28, listOf(0, 4, 7, 11, 14, 17),true),
        Chord("maj13", 29, listOf(0, 4, 7, 11, 14, 21),true),
        Chord("maj7b5", 30, listOf(0, 4, 6, 11),true),
        Chord("maj7sharp5", 31, listOf(0, 4, 8, 11),true),
        Chord("m6", 32, listOf(0, 3, 7, 9),false),
        Chord("m69", 33, listOf(0, 3, 7, 9, 14),false),
        Chord("m7", 34, listOf(0, 3, 7, 10),false),
        Chord("m7b5", 35, listOf(0, 3, 6, 10),false),
        Chord("m9", 36, listOf(0, 3, 7, 10, 14),false),
        Chord("m11", 37, listOf(0, 3, 7, 10, 14, 17),false),
        Chord("mmaj7", 38, listOf(0, 3, 7, 11),false),
        Chord("mmaj7b5", 39, listOf(0, 3, 6, 11),false),
        Chord("mmaj9", 40, listOf(0, 3, 7, 11, 14),false),
        Chord("mmaj11", 41, listOf(0, 3, 7, 11, 14, 17),false),
        Chord("add9", 42, listOf(0, 4, 7, 14),true),
        Chord("madd9", 43, listOf(0, 3, 7, 14),false),
    )

}