package com.jsdisco.soundscompose.presentation.chordsinfo

import android.content.Context
import android.media.SoundPool
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.soundscompose.data.ChordsRepository
import com.jsdisco.soundscompose.data.SoundsRepository
import com.jsdisco.soundscompose.data.models.Sound
import com.jsdisco.soundscompose.domain.models.FullChord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChordsInfoViewModel @Inject constructor(
    soundsRepo: SoundsRepository,
    repo: ChordsRepository,
    private val soundPool: SoundPool
) : ViewModel(){

    private val _soundResources = soundsRepo.soundResources
    private val sounds = mutableListOf<Sound>()

    val roots = repo.roots
    val chords = repo.chords

    var result = FullChord("C", "major", listOf(0,4,7), listOf(48,52,55), listOf("C", "E", "G"))

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

    fun getChord(root: String, chordName: String) {
        val chord = chords.find { it.name == chordName.replace("#", "sharp") }!!
        val intervals = chord.intervals
        val rootMk = roots.indexOf(root) + 48
        /* TODO: this will break if you add more sounds               v  */
        val midiKeys = intervals.map {if (it+rootMk > 71) it+rootMk - 24 else it+rootMk}
        val noteNames = midiKeys.map { mk -> roots[mk % 12] }

        result = FullChord(root, chordName, intervals, midiKeys, noteNames)
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
        result = FullChord("C", "major", listOf(0,4,7), listOf(48,52,55), listOf("C", "E", "G"))
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