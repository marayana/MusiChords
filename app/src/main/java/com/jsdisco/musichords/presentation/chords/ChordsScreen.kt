package com.jsdisco.musichords.presentation.chords

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.data.models.Chord
import com.jsdisco.musichords.data.models.Root
import com.jsdisco.musichords.data.models.Scale
import com.jsdisco.musichords.domain.models.ChordSolution
import com.jsdisco.musichords.presentation.chords.components.MusicSheet
import com.jsdisco.musichords.presentation.common.ButtonState
import com.jsdisco.musichords.presentation.common.components.GameButton


@Composable
fun ChordsScreen(
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit,
    viewModel: ChordsViewModel
) {

    val context = LocalContext.current
    viewModel.loadSounds(context)

    val chunks = 3

    fun handlePlayBtn() {
        viewModel.handlePlayBtn()
    }

    fun handleNextBtn(){
        viewModel.handleNextBtn()
    }

    fun playChord(chord: Chord){
        viewModel.playChord(chord)
    }

    fun checkAnswer(chordName: String){
        viewModel.checkAnswer(chordName)
    }

    fun setDelay(delay: Float){
        viewModel.setDelay(delay)
    }

    val noteStrings = stringArrayResource(id = R.array.notes_list_chordresult)
    val chordStrings = stringArrayResource(id = R.array.chords_list)

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
        ::setDelay,
        chunks,
        viewModel.solution,
        ::handlePlayBtn,
        ::handleNextBtn,
        ::checkAnswer,
        ::playChord,
        viewModel.roots,
        viewModel.scales,
        noteStrings,
        chordStrings
    )
}

@Composable
fun ChordsScreenUI(
    gameStatus: GameStatus,
    chordsList: List<Chord>,
    btnStates: List<ButtonState>,
    delay: Float,
    setDelay: (Float) -> Unit,
    chunks: Int,
    solution: ChordSolution,
    handlePlayBtn: () -> Unit,
    handleNextBtn: () -> Unit,
    checkAnswer: (String) -> Unit,
    playChord: (Chord) -> Unit,
    roots: List<Root>,
    scales: List<Scale>,
    noteStrings: Array<String>,
    chordStrings: Array<String>
) {

    val textIconNoDelay = stringResource(id = R.string.chords_icon_no_delay)
    val textIconMaxDelay = stringResource(id = R.string.chords_icon_max_delay)
    val textIconPlay = stringResource(id = R.string.chords_icon_play)
    val textIconNext = stringResource(id = R.string.chords_icon_next)
    val textGameBtns = stringArrayResource(id = R.array.chords_list)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp, bottom = 65.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_delay_min),
                tint = MaterialTheme.colors.onSecondary,
                contentDescription = textIconNoDelay,
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
                contentDescription = textIconMaxDelay,
                modifier = Modifier
                    .width(40.dp)
                    .alpha(0.7f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Image(
                    painter = painterResource(id = R.drawable.play_pink),
                    contentDescription = textIconPlay,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { handlePlayBtn() }
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = if (gameStatus == GameStatus.FOUND) R.drawable.next_pink else R.drawable.next_disabled),
                    contentDescription = textIconNext,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(enabled = gameStatus == GameStatus.FOUND) { handleNextBtn() }
                        .size(60.dp)
                )
            }

            Column {
                val solutionString = "${noteStrings[solution.rootStringIndex]} ${chordStrings[solution.chordStringIndex]}"
                Text(
                    text = if (gameStatus == GameStatus.FOUND) solutionString else "",
                    style = MaterialTheme.typography.h5
                )
                MusicSheet(roots, scales, solution, gameStatus)
            }
        }


        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {

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
                                    val btnText = textGameBtns[btnState.stringIndex]
                                    GameButton(
                                        btnState = btnState,
                                        btnText = btnText,
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
