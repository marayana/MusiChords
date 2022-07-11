package com.jsdisco.soundscompose.presentation.relativepitch

import android.content.Context
import android.media.SoundPool
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class RelativePitchViewModel @Inject constructor(
    private val repo: SoundsRepository,
    private val soundPool: SoundPool
) : ViewModel() {

    private val _soundResources = repo.soundResources
    private val sounds = mutableListOf<Sound>()

    val intervals = repo.intervals

    private var solution: Solution? = null

    private val isFound = mutableStateOf(true)

    val delay = mutableStateOf(0f)

    val buttonStates = initButtonStates().toMutableStateList()

    fun loadSounds(context: Context){
        if (sounds.isEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                _soundResources.forEach{
                    val soundId = soundPool.load(context, it.resId, 1)
                    sounds.add(Sound(it.name, it.midiKey, it.resId, soundId))
                }
            }
        }
    }

    fun newRoundAndPlay(){
        if (isFound.value){
            isFound.value = false

            resetBtnStates()
            createSolution()
        }
        playSounds()
    }

    fun checkAnswer(answer: String){
        if (answer == solution?.name){
            isFound.value = true
            buttonStates.find { it.index == answer.toInt()-1 }?.let { btnState ->
                //btnState.textColour = ButtonColour.textOnColour
                btnState.bgColour = ButtonColour.correctBg
            }
            buttonStates.forEach { btn -> btn.isDisabled = true }
        } else {
            buttonStates.find { it.index == answer.toInt()-1 }?.let { btnState ->
                //btnState.textColour = ButtonColour.textOnColour
                btnState.bgColour = ButtonColour.incorrectBg
            }
        }
    }

    private fun createSolution(){
        val solutionInterval = intervals.shuffled().last().halfTones

        val randomFirstMk = (0..12).shuffled().last() + 48
        val secondMk = randomFirstMk + solutionInterval

        solution = Solution(solutionInterval.toString(), randomFirstMk, listOf(randomFirstMk, secondMk))
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
                delay((delay.value * 1000).toLong())
            }
        }
    }

    private fun initButtonStates() : List<ButtonState> {
        val states = intervals.map{ interval ->
            ButtonState(interval.halfTones-1, interval.nameGerman)
        }
        return states
    }

    private fun resetBtnStates(){
        buttonStates.forEach { btn ->
            btn.textColour = ButtonColour.textOnInitial
            btn.bgColour = ButtonColour.initialBg
            btn.isDisabled = false
        }
    }

    fun reset(){
        isFound.value = true
        resetBtnStates()
    }

    fun setDelay(value: Float){
        delay.value = value
    }
}
