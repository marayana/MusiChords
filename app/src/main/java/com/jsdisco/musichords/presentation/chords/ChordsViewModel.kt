package com.jsdisco.musichords.presentation.chords

import android.content.Context
import android.media.SoundPool
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.musichords.data.ChordsRepository
import com.jsdisco.musichords.data.SoundStatus
import com.jsdisco.musichords.data.SoundsRepository
import com.jsdisco.musichords.data.local.models.SettingsChords
import com.jsdisco.musichords.data.local.models.ChordsSet
import com.jsdisco.musichords.data.models.Chord
import com.jsdisco.musichords.data.models.Sound
import com.jsdisco.musichords.domain.models.SelectBtnState
import com.jsdisco.musichords.domain.models.ChordSolution
import com.jsdisco.musichords.presentation.common.ButtonColour
import com.jsdisco.musichords.presentation.common.ButtonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

enum class GameStatus { INITIAL, GUESS, FOUND }


class ChordsViewModel(
    private val soundsRepo: SoundsRepository,
    private val chordsRepo: ChordsRepository,
    private val soundPool: SoundPool
) : ViewModel() {

    // SOUNDS
    private val _soundResources = soundsRepo.soundResources
    private val _currSoundIds = mutableListOf<Int>()
    private var soundsLoaded = 0
    val soundStatus = soundsRepo.soundStatus
    private var _playSoundsJob: Job? = null

    // CHORD SETTINGS
    val allChords = chordsRepo.chords
    private val _settingsChords = mutableStateOf(SettingsChords())
    val settingsChords: State<SettingsChords> = _settingsChords
    private val _chordsSets = mutableStateOf(emptyList<ChordsSet>())
    val chordsSets: State<List<ChordsSet>> = _chordsSets
    private val _activeChordsSet = mutableStateOf(ChordsSet(name = ""))
    val activeChordsSet: State<ChordsSet> = _activeChordsSet
    private val _activeChords = mutableStateOf(emptyList<Chord>())
    val activeChords: State<List<Chord>> = _activeChords


    // GAME VARS
    val gameStatus = mutableStateOf(GameStatus.INITIAL)
    var solution = ChordSolution("C",0, 0, chordsRepo.chords[0], 0, listOf(60, 64, 67), 0, 0, 0)
    val delay = mutableStateOf(0f)
    var buttonStates = emptyList<ButtonState>()


    // MUSIC SHEET
    val roots = chordsRepo.roots
    val scales = chordsRepo.scales


    init {
        initValues()
    }

    private fun initValues() {
        viewModelScope.launch(Dispatchers.IO) {
            chordsRepo.initChordsSets()
            chordsRepo.initSettingsChords()

            getSettingsChords()
            getChordsSets()
            getActiveChordsSet()
        }
    }

    // SETTINGS

    private fun getSettingsChords() {
        viewModelScope.launch {
            _settingsChords.value = chordsRepo.getSettingsChords()
        }
    }

    private fun getChordsSets() {
        viewModelScope.launch {
            _chordsSets.value = chordsRepo.getChordsSets()
        }
    }

    private fun getActiveChordsSet() {
        viewModelScope.launch {
            _activeChordsSet.value = chordsRepo.getActiveChordsSet()
            _activeChords.value = chordsRepo.getActiveChords(_activeChordsSet.value)
            buttonStates = initButtonStates(_activeChords.value)
        }
    }

    fun getChordsForChordsSet(set: ChordsSet): List<Chord> {
        return chordsRepo.getActiveChords(set)
    }

    fun selectChordsSet(set: ChordsSet) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val settings = _settingsChords.value
                settings.activeChordSetId = set.id
                chordsRepo.updateSettingsChords(settings)
                getActiveChordsSet()
            } catch (e: Exception) {
                Log.e("ChordsViewModel", "Error in selectChordsSet(): $e")
            }
        }
    }

    fun addChordsSet(title: String, selectedChords: List<SelectBtnState>) {

        viewModelScope.launch {
            val set = ChordsSet(
                name = title,
                isCustom = true,
                c1 = selectedChords[0].name,
                c2 = selectedChords[1].name,
                c3 = if (selectedChords.size > 2) selectedChords[2].name else null,
                c4 = if (selectedChords.size > 3) selectedChords[3].name else null,
                c5 = if (selectedChords.size > 4) selectedChords[4].name else null,
                c6 = if (selectedChords.size > 5) selectedChords[5].name else null,
                c7 = if (selectedChords.size > 6) selectedChords[6].name else null,
                c8 = if (selectedChords.size > 7) selectedChords[7].name else null,
                c9 = if (selectedChords.size > 8) selectedChords[8].name else null,
                c10 = if (selectedChords.size > 9) selectedChords[9].name else null,
                c11 = if (selectedChords.size > 10) selectedChords[10].name else null,
                c12 = if (selectedChords.size > 11) selectedChords[11].name else null,
            )
            chordsRepo.addChordsSet(set)
            _chordsSets.value = chordsRepo.getChordsSets()
        }
    }

    fun deleteChordsSet(set: ChordsSet) {
        viewModelScope.launch(Dispatchers.IO) {
            chordsRepo.deleteChordsSet(set)
            _chordsSets.value = chordsRepo.getChordsSets()
        }
    }

    fun togglePlayChordOnTap() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val settings = _settingsChords.value
                settings.playChordOnTap = !settings.playChordOnTap
                chordsRepo.updateSettingsChords(settings)
            } catch (e: Exception) {
                Log.e("ChordsViewModel", "Error in togglePlayChordOnTap(): $e")
            }
        }
    }

    fun toggleInversions() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val settings = _settingsChords.value
                settings.withInversions = !settings.withInversions
                chordsRepo.updateSettingsChords(settings)
            } catch (e: Exception) {
                Log.e("ChordsViewModel", "Error in toggleInversions(): $e")
            }
        }
    }

    fun toggleOpenPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val settings = _settingsChords.value
                settings.withOpenPosition = !settings.withOpenPosition
                chordsRepo.updateSettingsChords(settings)
            } catch (e: Exception) {
                Log.e("ChordsViewModel", "Error in toggleOpenPosition(): $e")
            }
        }
    }


    // GAME

    fun loadSounds(context: Context) {
        if (soundsRepo.sounds.isEmpty()) {

            viewModelScope.launch(Dispatchers.IO) {
                soundPool.setOnLoadCompleteListener { _, _, status ->
                    if (status == 0) {
                        soundsLoaded++
                    }
                    if (status != 0){
                        soundsRepo.soundStatus.value = SoundStatus.FAILURE
                        return@setOnLoadCompleteListener
                    }
                    if (soundsLoaded == _soundResources.size) {
                        soundsRepo.soundStatus.value = SoundStatus.SUCCESS
                    }
                }
                _soundResources.forEach {
                    val soundId = soundPool.load(context, it.resId, 1)
                    soundsRepo.sounds.add(Sound(it.name, it.midiKey, it.resId, soundId))
                }
            }
        }

    }

    private fun initButtonStates(chords: List<Chord>): List<ButtonState> {
        val states = chords.map{ chord ->
            val stringIndex =
                chordsRepo.chords.indices.find { chordsRepo.chords[it].name == chord.name } ?: 0
            ButtonState(chord.name, stringIndex)
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

    private fun createSolution() {
        val randomChord = _activeChords.value.shuffled().last()
        val randomRoot = (0..11).shuffled().last()

        val rootName = if (randomChord.isMajor) roots[randomRoot].rootMajor else roots[randomRoot].rootMinor
        val noteIndex = chordsRepo.notes.indexOf(rootName)
        val chordIndex = chordsRepo.chords.map { it.name }.indexOf(randomChord.name)

        val intervals = randomChord.intervals
        val inversions = if (settingsChords.value.withInversions) (0..3).random() else 0
        val openPositionIndex = if (settingsChords.value.withOpenPosition) (intervals.indices).random() else 0

        val finalMidiKeys = getMidiKeysForChordSolution(intervals, randomRoot, inversions, openPositionIndex)

        solution = ChordSolution(
            rootName,
            noteIndex,
            chordIndex,
            randomChord,
            randomRoot,
            finalMidiKeys,
            inversions,
            openPositionIndex
        )
    }

    private fun getMidiKeysForChordSolution(
        intervals: List<Int>,
        root: Int,
        inversions: Int,
        openPositionIndex: Int
    ): List<Int> {

        val inversedIntervals = inverseIntervals(intervals, inversions)
        val withOpenPosition = applyOpenPosition(inversedIntervals, openPositionIndex)

        return withOpenPosition.map { it + soundsRepo.lowestMidiKey + root }
    }

    private fun getMidiKeysForChord(intervals: List<Int>): List<Int> {

        val inversedIntervals = inverseIntervals(intervals, solution.inversions)

        /** https://music.stackexchange.com/questions/90171/does-every-chord-have-inversions*/
        val withOpenPosition = applyOpenPosition(inversedIntervals, solution.openPositionIndex)

        return withOpenPosition.map { it + soundsRepo.lowestMidiKey + solution.root }

    }

    private fun applyOpenPosition(intervals: List<Int>, openPositionIndex: Int): List<Int>{
        val openPositionIntervals = mutableListOf<Int>()

        for ((i, interval) in intervals.withIndex()){
            openPositionIntervals.add(if(i < openPositionIndex) interval else interval + 12)
        }

        return openPositionIntervals
    }

    private fun inverseIntervals(intervals: List<Int>, times: Int): List<Int> {

        var currentIntervals = intervals


        for (i in (0 until times)){

            val scaledDownIntervals = currentIntervals.map { it % 12 }.sorted()
            val inversedIntervals = mutableListOf<Int>()

            for (interval in currentIntervals) {
                val currIndex = scaledDownIntervals.indexOf(interval % 12)
                val nextIndex = if (currIndex == intervals.size-1) 0 else currIndex + 1

                var nextInterval = scaledDownIntervals[nextIndex]
                if (inversedIntervals.isNotEmpty()) {
                    while (nextInterval < inversedIntervals.last()) {
                        nextInterval += 12
                    }
                }
                inversedIntervals.add(nextInterval)
            }

            currentIntervals = inversedIntervals
        }

        val transpose = floor(times.toDouble() / intervals.size.toDouble()).toInt()

        return currentIntervals.map { it + 12 * transpose }
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

    fun playChord(chord: Chord) {
        if (settingsChords.value.playChordOnTap) {
            playSounds(getCurrSounds(getMidiKeysForChord(chord.intervals)))
        }
    }

    fun handlePlayBtn() {
        if (gameStatus.value == GameStatus.INITIAL) {
            gameStatus.value = GameStatus.GUESS
            createSolution()
            setButtonStates()
        }
        playSounds(getCurrSounds(getMidiKeysForChord(solution.chord.intervals)))
    }

    fun handleNextBtn() {
        gameStatus.value = GameStatus.GUESS
        createSolution()
        setButtonStates()
        playSounds(getCurrSounds(getMidiKeysForChord(solution.chord.intervals)))
    }

    fun checkAnswer(answer: String) {
        if (answer == solution.chord.name) {
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

    fun reset() {
        gameStatus.value = GameStatus.INITIAL
        _currSoundIds.clear()
        setButtonStates()
    }

    fun setDelay(value: Float) {
        delay.value = value
    }
}