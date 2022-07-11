package com.jsdisco.soundscompose.presentation.chords

import android.content.Context
import android.media.SoundPool
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.soundscompose.data.ChordsRepository
import com.jsdisco.soundscompose.data.SoundsRepository
import com.jsdisco.soundscompose.data.models.Sound
import com.jsdisco.soundscompose.domain.models.Solution
import com.jsdisco.soundscompose.presentation.common.components.ButtonColour
import com.jsdisco.soundscompose.presentation.common.components.ButtonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChordsViewModel @Inject constructor(
    private val soundsRepo: SoundsRepository,
    private val chordsRepo: ChordsRepository,
    private val soundPool: SoundPool
) : ViewModel() {
    

    private val _soundResources = soundsRepo.soundResources
    private val sounds = mutableListOf<Sound>()

    val chords = chordsRepo.chordsInGame

    private var solution: Solution? = null

    private val isFound = mutableStateOf(true)

    val delay = mutableStateOf(0f)

    val buttonStates = initButtonStates().toMutableStateList()

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

    fun newRoundAndPlay(){
        if (isFound.value){
            isFound.value = false

            resetBtnColours()
            toggleBtnEnabled(false)
            createSolution()
        }
        playSounds()
    }

    fun checkAnswer(answer: String){
        if (answer == solution?.name){
            isFound.value = true
            buttonStates.find { it.text == answer }?.let { btnState ->
                //btnState.textColour = ButtonColour.textOnColour
                //btnState.bgColour = ButtonColour.correctBg
                btnState.borderColour = ButtonColour.correctBg
            }

            buttonStates.forEach { btnState ->
                btnState.isDisabled = true
                btnState.bgColour = ButtonColour.disabledBg
            }
        } else {
            buttonStates.find { it.text == answer }?.let { btnState ->
                //btnState.textColour = ButtonColour.textOnColour
                //btnState.bgColour = ButtonColour.incorrectBg
                btnState.borderColour = ButtonColour.incorrectBg
                btnState.isDisabled = true
                btnState.bgColour = ButtonColour.disabledBg
            }
        }
    }

    private fun createSolution(){
        val solutionRoot = (0..11).shuffled().last() + 48
        val solutionChord = chords.shuffled().last()
        val solutionMidiKeys = mutableListOf<Int>()

        solutionChord.intervals.forEach { interval ->
            var mK = solutionRoot + interval
            if (mK > 72){
                /* TODO this is messy and will break if you add more sounds */
                mK -= 24
            }
            solutionMidiKeys.add(mK)
        }

        solution = Solution(solutionChord.name,solutionRoot, solutionMidiKeys)
    }

    private fun playSounds() {
        val soundsList = mutableListOf<Sound>()
        solution?.midiKeys?.forEach { mK ->
            val sound = sounds.find { it.midiKey == mK }
            if (sound != null){
                soundsList.add(sound)
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            soundsList.forEach { sound ->
                soundPool.play(sound.sound,1f, 1f, 1, 0, 1f )
                delay((delay.value * 300).toLong())
            }
        }
    }

    private fun initButtonStates() : List<ButtonState>{
        val states = chords.mapIndexed { i, chord ->
            ButtonState(i, chord.name)
        }
        return states
    }

    private fun resetBtnColours(){
        buttonStates.forEach { btnState ->
            btnState.textColour = ButtonColour.textOnInitial
            btnState.bgColour = ButtonColour.disabledBg
            btnState.borderColour = ButtonColour.initialBorderColour
        }
    }

    private fun toggleBtnEnabled(value: Boolean){
        buttonStates.forEach { btnState ->
            btnState.isDisabled = value
            btnState.bgColour = if (value) ButtonColour.disabledBg else ButtonColour.initialBg
        }
    }

    fun reset(){
        isFound.value = true
        resetBtnColours()
        toggleBtnEnabled(true)
    }

    fun setDelay(value: Float){
        delay.value = value
    }
}