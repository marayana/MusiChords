package com.jsdisco.soundscompose.presentation.chords

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.local.models.SettingsChords
import com.jsdisco.soundscompose.data.models.Chord
import com.jsdisco.soundscompose.data.models.Note
import com.jsdisco.soundscompose.data.models.Root
import com.jsdisco.soundscompose.data.models.Scale
import com.jsdisco.soundscompose.domain.models.ChordSolution
import com.jsdisco.soundscompose.presentation.chords.components.MusicSheet
import com.jsdisco.soundscompose.presentation.common.ButtonState
import com.jsdisco.soundscompose.presentation.common.components.GameButton
import com.jsdisco.soundscompose.presentation.common.navigation.GameScreen
import com.jsdisco.soundscompose.presentation.common.navigation.Screen


@Composable
fun ChordsScreen(
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit,
    viewModel: ChordsViewModel
) {

    viewModel.loadSounds(LocalContext.current)

    val chunks = 3

    fun handlePlayBtn() {
        viewModel.handlePlayBtn()
    }

    val handleNextBtn = { viewModel.handleNextBtn() }

    val playChord = { chord: Chord -> viewModel.playChord(chord) }

    val checkAnswer = { chordName: String -> viewModel.checkAnswer(chordName) }

    val setDelay = { delay: Float -> viewModel.setDelay(delay) }

    val title = stringResource(id = R.string.title_chords)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
        viewModel.reset()
    }

    ChordsScreenUI(
        viewModel.gameStatus.value,
        viewModel.activeChords.value,
        viewModel.buttonStates,
        viewModel.delay.value,
        chunks,
        viewModel.solution,
        ::handlePlayBtn,
        handleNextBtn,
        checkAnswer,
        setDelay,
        playChord,
        viewModel.currMidiKeys,
        viewModel.roots,
        viewModel.scales,
        viewModel.notesWhite,
        viewModel.notesBlack,
        viewModel.soundStatus.value
    )
}

@Composable
fun ChordsScreenUI(
    gameStatus: GameStatus,
    chordsList: List<Chord>,
    btnStates: List<ButtonState>,
    delay: Float,
    chunks: Int,
    solution: ChordSolution,
    handlePlayBtn: () -> Unit,
    handleNextBtn: () -> Unit,
    checkAnswer: (String) -> Unit,
    setDelay: (Float) -> Unit,
    playChord: (Chord) -> Unit,
    currMidiKeys: List<Int>,
    roots: List<Root>,
    scales: List<Scale>,
    notesWhite: List<Note>,
    notesBlack: List<Note>,
    soundStatus: SoundStatus
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp, bottom = 65.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (soundStatus == SoundStatus.SUCCESS) Arrangement.SpaceBetween else Arrangement.Center
    ) {
        if (soundStatus == SoundStatus.LOADING) {
            CircularProgressIndicator()
            Text(text = "loading sounds...", style = MaterialTheme.typography.h5) /* TODO */
        } else if (soundStatus == SoundStatus.FAILURE) {
            Text(
                text = "an error occured while loading sounds",
                style = MaterialTheme.typography.h5
            ) /* TODO */
        } else {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delay_min),
                    tint = MaterialTheme.colors.onSecondary,
                    contentDescription = "no delay", /* TODO */
                    modifier = Modifier
                        .width(40.dp)
                        .alpha(0.7f)
                )
                Slider(
                    value = delay,
                    onValueChange = { value -> setDelay(value) },
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delay_max),
                    tint = MaterialTheme.colors.onSecondary,
                    contentDescription = "no delay", /* TODO */
                    modifier = Modifier
                        .width(40.dp)
                        .alpha(0.7f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Image(
                        painter = painterResource(id = R.drawable.replay_pink),
                        contentDescription = "play chord",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { handlePlayBtn() }
                            .size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = painterResource(id = if (gameStatus == GameStatus.FOUND) R.drawable.next_pink else R.drawable.next_disabled),
                        contentDescription = "next round",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(enabled = gameStatus == GameStatus.FOUND) { handleNextBtn() }
                            .size(60.dp)
                    )
                }

                Column() {
                    Text(
                        text = if (gameStatus == GameStatus.FOUND) solution.displayName else "",
                        style = MaterialTheme.typography.h5
                    )
                    MusicSheet(roots, scales, solution, gameStatus)
                }
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    val nestedChordsList = chordsList.chunked(chunks)

                    for ((i, row) in nestedChordsList.withIndex()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (j in (0 until 3)) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp)
                                ) {
                                    if (j < row.size) {
                                        val chord = row[j]
                                        val btnState = btnStates[i * chunks + j]
                                        GameButton(
                                            btnState = btnState,
                                            onClick = {
                                                playChord(chord)
                                                if (gameStatus == GameStatus.GUESS) {
                                                    checkAnswer(chord.name)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
