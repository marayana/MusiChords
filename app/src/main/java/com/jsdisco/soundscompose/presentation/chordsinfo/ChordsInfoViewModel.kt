package com.jsdisco.soundscompose.presentation.chordsinfo

import android.content.Context
import android.media.SoundPool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.soundscompose.data.ChordsRepository
import com.jsdisco.soundscompose.data.SoundsRepository
import com.jsdisco.soundscompose.data.models.Chord
import com.jsdisco.soundscompose.data.models.Root
import com.jsdisco.soundscompose.data.models.Scale
import com.jsdisco.soundscompose.data.models.Sound
import com.jsdisco.soundscompose.domain.models.ChordSearchResult
import com.jsdisco.soundscompose.domain.models.ChordSolution
import com.jsdisco.soundscompose.presentation.chords.components.Accidental
import com.jsdisco.soundscompose.presentation.chords.components.SheetNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChordsInfoViewModel (
    soundsRepo: SoundsRepository,
    private val repo: ChordsRepository,
    private val soundPool: SoundPool
) : ViewModel(){

    private val _soundResources = soundsRepo.soundResources
    private val sounds = mutableListOf<Sound>()

    val roots = repo.roots
    val chords = repo.chords

    var result = ChordSearchResult("C", "major", listOf(0,4,7), listOf(60,64,67),listOf(60,64,67), listOf("C", "E", "G"))

    val notesWhite = repo.notesWhite
    val notesBlack = repo.notesBlack


    fun loadSounds(context: Context){
        if (sounds.isEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                _soundResources.forEach{
                    val soundId = soundPool.load(context, it.resId, 1)
                    sounds.add(Sound(it.name,it.midiKey, it.resId, soundId))
                }
            }
        }
    }

    fun getChord(root: Root, chordName: String) {
        val chord = chords.find { it.name == chordName.replace("#", "sharp") }!!
        val intervals = chord.intervals
        val rootMk = roots.indexOf(root) + 60
        val midiKeys = intervals.map { it + rootMk }
        val midiKeysPiano = intervals.map {if (it+rootMk >= 84) it+rootMk-24 else it+rootMk}
        //val noteNames = midiKeys.map { mk -> roots[mk % 12].displayName }
        val noteNames = getNotes(root, chord)

        result = ChordSearchResult(root.displayName, chordName, intervals, midiKeys, midiKeysPiano, noteNames)
    }

    private fun getNotes(root: Root, chord: Chord) : List<String>{
        val notes = mutableListOf<String>()
        val scale = repo.scales.find { it.root == if (chord.isMajor) root.rootMajor else root.rootMinor }
            ?: return emptyList()
        for (interval in chord.intervals){
            notes.add(getNote(interval, scale, chord))
        }
        return notes
    }

    private fun getNote(
        interval: Int,
        scale: Scale,
        chord: Chord
    ): String {

        val noteName: String

        if (interval == 2){
            // second note: sus2
            noteName = scale.major[1]
        } else if (interval == 3 && !chord.isMajor){
            // secondNote: minor
            noteName = scale.minor[2]
        } else if (interval == 3 && chord.isMajor){
            // secondNote: dim
            noteName = scale.minor[2]
        } else if (interval == 4){
            // second note: third
            noteName = scale.major[2]
        } else if (interval == 5){
            // second note: sus4
            noteName = scale.major[3]
        } else if (interval == 6){
            // thirdNote: dim or b5
            val fifth = scale.major[4]
            noteName = if (fifth.contains("#")) fifth[0].toString() else fifth + "b"
        } else if (interval == 7){
            // third note: perfect fifth
            noteName = scale.major[4]
        } else if (interval == 8){
            // thirdNote: aug or #5
            val fifth = scale.major[4]
            noteName = if (fifth.contains("b")) fifth[0].toString() else "$fifth#"
        } else if (interval == 9 && chord.name.contains("dim")){
            // fourth note: dim7
            val dom7 = scale.minor[6]
            noteName = if (dom7.contains("#")) dom7[0].toString() else  dom7 + "b"
        } else if (interval == 9){
            // fourth note: 6
            noteName = scale.major[5]
        } else if (interval == 10){
            // fourth note: dom7
            noteName = scale.minor[6]
        } else if (interval == 11) {
            // fourth note: maj7
            noteName = scale.major[6]
        } else if (interval == 13){
            // fifth note: b9
            val ninth = scale.major[1]
            noteName = if (ninth.contains("#")) ninth[0].toString() else ninth + "b"
        } else if (interval == 14){
            // fifth note: 9
            noteName = scale.major[1]
        } else if (interval == 15){
            // fifth note: #9
            val ninth = scale.major[1]
            noteName = if (ninth.contains("b")) ninth[0].toString() else "$ninth#"
        } else if (interval == 17){
            // sixth note: 11
            noteName = scale.major[3]
        } else if (interval == 18){
            // sixth note: #11
            val eleventh = scale.major[3]
            noteName = if (eleventh.contains("b")) eleventh[0].toString() else "$eleventh#"
        } else if (interval == 21){
            // seventh note: 13
            noteName = scale.major[5]
        } else {
            noteName = scale.major[0]
        }

        return noteName
    }

    fun playSounds() {
        val soundsList = mutableListOf<Sound>()
        result.midiKeys.forEach { mK ->
            val sound = sounds.find { it.midiKey == mK }
            if (sound != null){
                soundsList.add(sound)
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            soundsList.forEach { sound ->
                soundPool.play(sound.sound,1f, 1f, 1, 0, 1f )
            }
        }
    }

    fun reset(){
        result = ChordSearchResult("C", "major", listOf(0,4,7), listOf(60,64,67),listOf(60,64,67), listOf("C", "E", "G"))
    }
    /*
    fun getChord(root: String, chordName: String): FullChord {
        val chord = chords.find { it.name == chordName.replace("#", "sharp") }!!
        val intervals = chord.intervals
        val rootMk = roots.indexOf(root) + 48
        /* TODO: this will break if you add more sounds               v  */
        val midiKeys = intervals.map {if (it+rootMk > 71) it+rootMk - 24 else it+rootMk}
        val noteNames = midiKeys.map { mk -> roots[mk % 12] }

        return FullChord(root, chordName, intervals, midiKeys, noteNames)
    }*/
}