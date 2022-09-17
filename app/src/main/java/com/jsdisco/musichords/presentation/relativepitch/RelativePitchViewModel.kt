package com.jsdisco.musichords.presentation.relativepitch

import android.content.Context
import android.media.SoundPool
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.musichords.data.RelativePitchRepository
import com.jsdisco.musichords.data.SoundStatus
import com.jsdisco.musichords.data.SoundsRepository
import com.jsdisco.musichords.data.local.models.SettingsRelativePitch
import com.jsdisco.musichords.data.models.Interval
import com.jsdisco.musichords.data.models.Sound
import com.jsdisco.musichords.domain.models.IntervalSolution
import com.jsdisco.musichords.presentation.chords.GameStatus
import com.jsdisco.musichords.presentation.common.ButtonColour
import com.jsdisco.musichords.presentation.common.ButtonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RelativePitchViewModel (
    private val soundsRepo: SoundsRepository,
    private val rpRepo: RelativePitchRepository,
    private val soundPool: SoundPool
) : ViewModel() {

    // SOUNDS
    private val _soundResources = soundsRepo.soundResources
    private val _currSoundIds = mutableListOf<Int>()
    private var soundsLoaded = 0
    val soundStatus = soundsRepo.soundStatus
    private var _playSoundsJob: Job? = null


    // SETTINGS
    val allIntervals = rpRepo.intervals
    private val _settingsRP = mutableStateOf(SettingsRelativePitch())
    val settingsRP: State<SettingsRelativePitch> = _settingsRP
    private val _activeIntervals = mutableStateOf(emptyList<Interval>())
    val activeIntervals: State<List<Interval>> = _activeIntervals

    // MUSIC SHEET
    val roots = rpRepo.roots

    // GAME VARS
    val gameStatus = mutableStateOf(GameStatus.INITIAL)
    var solution = IntervalSolution(allIntervals[0], false, listOf(60,60))
    val delay = mutableStateOf(0f)
    var buttonStates = emptyList<ButtonState>()

    init {
        initValues()
    }

    private fun initValues(){
        viewModelScope.launch(Dispatchers.IO) {
            rpRepo.initSettingsRP()

            getSettingsRP()

        }
    }

    // SETTINGS

    private fun getSettingsRP(){
        viewModelScope.launch {
            val settings = rpRepo.getSettingsRP()
            _settingsRP.value = settings
            _activeIntervals.value = getActiveIntervals(settings)
            buttonStates = initButtonStates(_activeIntervals.value)
        }
    }

    private fun getActiveIntervals(settings: SettingsRelativePitch) : List<Interval> {
        val intervals = mutableListOf<Interval>()
        if (settings.interval1){
            intervals.add(allIntervals[0])
        }
        if (settings.interval2){
            intervals.add(allIntervals[1])
        }
        if (settings.interval3){
            intervals.add(allIntervals[2])
        }
        if (settings.interval4){
            intervals.add(allIntervals[3])
        }
        if (settings.interval5){
            intervals.add(allIntervals[4])
        }
        if (settings.interval6){
            intervals.add(allIntervals[5])
        }
        if (settings.interval7){
            intervals.add(allIntervals[6])
        }
        if (settings.interval8){
            intervals.add(allIntervals[7])
        }
        if (settings.interval9){
            intervals.add(allIntervals[8])
        }
        if (settings.interval10){
            intervals.add(allIntervals[9])
        }
        if (settings.interval11){
            intervals.add(allIntervals[10])
        }
        if (settings.interval12){
            intervals.add(allIntervals[11])
        }
        return intervals
    }

    fun togglePlayIntervalOnTap(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val settings = _settingsRP.value
                settings.playIntervalOnTap = !settings.playIntervalOnTap
                rpRepo.updateSettingsRP(settings)
            } catch(e: Exception){
                Log.e("RelativePitchViewModel", "Error in togglePlayIntervalOnTap: $e")
            }
        }
    }

    fun toggleCompoundIntervals(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val settings = _settingsRP.value
                settings.withCompoundIntervals = !settings.withCompoundIntervals
                rpRepo.updateSettingsRP(settings)
            } catch(e: Exception){
                Log.e("RelativePitchViewModel", "Error in toggleCompoundIntervals: $e")
            }
        }
    }

    fun toggleNotationAbbr(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val settings = _settingsRP.value
                settings.isNotationAbbr = !settings.isNotationAbbr
                rpRepo.updateSettingsRP(settings)
            } catch(e: Exception){
                Log.e("RelativePitchViewModel", "Error in toggleNotationAbbr: $e")
            }
        }
    }

    fun toggleActiveIntervals(interval: Interval){
        viewModelScope.launch(Dispatchers.IO){
            try {
                when (interval.halfTones) {
                    1 -> {
                        settingsRP.value.interval1 = !settingsRP.value.interval1
                    }
                    2 -> {
                        settingsRP.value.interval2 = !settingsRP.value.interval2
                    }
                    3 -> {
                        settingsRP.value.interval3 = !settingsRP.value.interval3
                    }
                    4 -> {
                        settingsRP.value.interval4 = !settingsRP.value.interval4
                    }
                    5 -> {
                        settingsRP.value.interval5 = !settingsRP.value.interval5
                    }
                    6 -> {
                        settingsRP.value.interval6 = !settingsRP.value.interval6
                    }
                    7 -> {
                        settingsRP.value.interval7 = !settingsRP.value.interval7
                    }
                    8 -> {
                        settingsRP.value.interval8 = !settingsRP.value.interval8
                    }
                    9 -> {
                        settingsRP.value.interval9 = !settingsRP.value.interval9
                    }
                    10 -> {
                        settingsRP.value.interval10 = !settingsRP.value.interval10
                    }
                    11 -> {
                        settingsRP.value.interval11 = !settingsRP.value.interval11
                    }
                    12 -> {
                        settingsRP.value.interval12 = !settingsRP.value.interval12
                    }
                }
                rpRepo.updateSettingsRP(settingsRP.value)
                _activeIntervals.value = getActiveIntervals(settingsRP.value)
                buttonStates = initButtonStates(_activeIntervals.value)

            } catch(e: Exception){
                Log.e("RelativePitchViewModel", "Error in toggleActiveIntervals: $e")
            }
        }
    }

    fun loadSounds(context: Context){
        if (soundsRepo.sounds.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                soundPool.setOnLoadCompleteListener { _, _, status ->
                    if (status == 0) {
                        soundsLoaded++
                    }
                    if (status != 0){
                        soundStatus.value = SoundStatus.FAILURE
                        return@setOnLoadCompleteListener
                    }
                    if (soundsLoaded == _soundResources.size) {
                        soundStatus.value = SoundStatus.SUCCESS
                    }
                }
                _soundResources.forEach {
                    val soundId = soundPool.load(context, it.resId, 1)
                    soundsRepo.sounds.add(Sound(it.name, it.midiKey, it.resId, soundId))
                }
            }
        }
    }

    private fun initButtonStates(intervals: List<Interval>) : List<ButtonState> {
        val states = intervals.map{ interval ->
            ButtonState(interval.halfTones.toString(), interval.stringIndex)
        }
        return states
    }

    private fun setButtonStates() {
        when (gameStatus.value) {
            GameStatus.INITIAL -> {
                buttonStates.forEach { btnState ->
                    btnState.isEnabled = false
                    btnState.borderColour = ButtonColour.transparentBorderColour
                }
            }
            GameStatus.FOUND -> {
                buttonStates.forEach { btnState ->
                    btnState.borderColour = ButtonColour.transparentBorderColour
                }
            }
            GameStatus.GUESS -> {
                buttonStates.forEach { btnState ->
                    btnState.isEnabled = true
                    btnState.borderColour = ButtonColour.transparentBorderColour
                }
            }
        }
    }

    private fun createSolution(){
        val solutionInterval = _activeIntervals.value.shuffled().last()

        val isCompound = if (settingsRP.value.withCompoundIntervals) {
            listOf(true, false).shuffled().last()
        } else {
            false
        }

        val randomFirstMk = (0..12).shuffled().last() + soundsRepo.lowestMidiKey

        val secondMk = if (isCompound) randomFirstMk + solutionInterval.halfTones + 12 else randomFirstMk + solutionInterval.halfTones


        solution = IntervalSolution(solutionInterval, isCompound, listOf(randomFirstMk, secondMk))
    }

    private fun getCurrSounds(midiKeys: List<Int>): List<Sound> {
        val sounds = mutableListOf<Sound>()
        midiKeys.forEach { mK ->
            val sound = soundsRepo.sounds.find { it.midiKey == mK }
            if (sound != null) {
                sounds.add(sound)
            }
        }
        return sounds
    }

    private fun playSounds(currSounds: List<Sound>) {
        _playSoundsJob?.cancel()

        _playSoundsJob = viewModelScope.launch(Dispatchers.Default) {
            _currSoundIds.forEach { id ->
                soundPool.stop(id)
            }
            _currSoundIds.clear()
            currSounds.forEach { sound ->
                val id = soundPool.play(sound.sound, 1f, 1f, 1, 0, 1f)
                _currSoundIds.add(id)
                delay((delay.value * 300).toLong())
            }
        }
    }

    fun playInterval(interval: Interval){
        if (settingsRP.value.playIntervalOnTap){
            if (solution.isCompound){
                playSounds(getCurrSounds(listOf(solution.midiKeys[0], solution.midiKeys[0] + interval.halfTones + 12)))

            } else {
                playSounds(getCurrSounds(listOf(solution.midiKeys[0], solution.midiKeys[0] + interval.halfTones)))

            }
        }
    }

    fun handlePlayBtn(){
        if (gameStatus.value == GameStatus.INITIAL) {
            gameStatus.value = GameStatus.GUESS
            createSolution()
            setButtonStates()
        }
        playSounds(getCurrSounds(solution.midiKeys))
    }

    fun handleNextBtn() {
        gameStatus.value = GameStatus.GUESS
        createSolution()
        setButtonStates()
        playSounds(getCurrSounds(solution.midiKeys))
    }

    fun checkAnswer(answer: String){
        if (answer == solution.interval.halfTones.toString()) {
            gameStatus.value = GameStatus.FOUND

            buttonStates.find { it.text == answer }?.let { btnState ->
                btnState.borderColour = ButtonColour.correctBg
            }
        } else {
            buttonStates.find { it.text == answer }?.let { btnState ->
                btnState.borderColour = ButtonColour.incorrectBg
            }
        }
    }

    fun reset(){
        gameStatus.value = GameStatus.INITIAL
        _currSoundIds.clear()
        setButtonStates()
    }

    fun setDelay(value: Float){
        delay.value = value
    }
}
