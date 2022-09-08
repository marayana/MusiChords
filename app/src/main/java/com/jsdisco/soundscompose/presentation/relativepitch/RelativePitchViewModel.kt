package com.jsdisco.soundscompose.presentation.relativepitch

import android.content.Context
import android.media.SoundPool
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.soundscompose.data.SoundsRepository
import com.jsdisco.soundscompose.data.models.Sound
import com.jsdisco.soundscompose.domain.models.IntervalSolution
import com.jsdisco.soundscompose.presentation.common.ButtonColour
import com.jsdisco.soundscompose.presentation.common.ButtonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RelativePitchViewModel (
    repo: SoundsRepository,
    private val soundPool: SoundPool
) : ViewModel() {

    private val _soundResources = repo.soundResources
    private val _sounds = mutableListOf<Sound>()
    private val _currSoundIds = mutableListOf<Int>()

    val intervals = repo.intervals

    private var solution: IntervalSolution? = null

    private val isFound = mutableStateOf(true)

    val delay = mutableStateOf(0f)

    val buttonStates = initButtonStates().toMutableStateList()

    var soundsLoaded = 0
    var allSoundsLoaded = mutableStateOf(false)

    fun loadSounds(context: Context){
        if (_sounds.isEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                soundPool.setOnLoadCompleteListener{ soundPool, sampleId, status ->
                    if (status == 0){
                        soundsLoaded++
                    }
                    if (soundsLoaded == _soundResources.size){
                        allSoundsLoaded.value = true
                    }

                }
                _soundResources.forEach{
                    val soundId = soundPool.load(context, it.resId, 1)
                    _sounds.add(Sound(it.name, it.midiKey, it.resId, soundId))
                }
            }
        }
    }

    fun newRoundAndPlay(){
        if (isFound.value){
            isFound.value = false

            resetBtnColours()
            toggleBtnsEnabled(true)
            createSolution()
        }
        playSounds()
    }

    fun checkAnswer(answer: String){
        if (answer == solution?.name){
            isFound.value = true
            buttonStates.find { it.index == answer.toInt()-1 }?.let { btnState ->
                //btnState.textColour = ButtonColour.textOnColour
                //btnState.bgColour = ButtonColour.correctBg
                btnState.borderColour = ButtonColour.correctBg
            }
            toggleBtnsEnabled(false)
        } else {
            buttonStates.find { it.index == answer.toInt()-1 }?.let { btnState ->
                //btnState.textColour = ButtonColour.textOnColour
                btnState.borderColour = ButtonColour.incorrectBg
                btnState.isEnabled = false
            }
        }
    }

    private fun createSolution(){
        val solutionInterval = intervals.shuffled().last().halfTones

        val randomFirstMk = (0..12).shuffled().last() + 48
        val secondMk = randomFirstMk + solutionInterval

        solution = IntervalSolution(solutionInterval.toString(), listOf(randomFirstMk, secondMk))
    }

    private fun playSounds() {
        val soundsList = mutableListOf<Sound>()
        solution?.midiKeys?.forEach { mK ->
            val sound = _sounds.find { it.midiKey == mK }
            if (sound != null){
                soundsList.add(sound)
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            _currSoundIds.forEach { id ->
                soundPool.stop(id)
            }
            _currSoundIds.clear()
            soundsList.forEach { sound ->
                val id = soundPool.play(sound.sound,1f, 1f, 1, 0, 1f )
                _currSoundIds.add(id)
                delay((delay.value * 1000).toLong())
            }
        }
    }

    private fun initButtonStates() : List<ButtonState> {
        val states = intervals.map{ interval ->
            ButtonState(interval.halfTones-1, (interval.halfTones).toString(), 0) /* TODO stringIndex */
        }
        return states
    }

    private fun resetBtnColours(){
        buttonStates.forEach { btnState ->
            btnState.borderColour = ButtonColour.transparentBorderColour
        }
    }

    private fun toggleBtnsEnabled(value: Boolean){
        buttonStates.forEach { btnState ->
            btnState.isEnabled = value
        }
    }

    fun reset(){
        isFound.value = true
        resetBtnColours()
        toggleBtnsEnabled(false)
    }

    fun setDelay(value: Float){
        delay.value = value
    }
}
