package com.jsdisco.musichords.presentation.relativepitch

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
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
import com.jsdisco.musichords.data.models.Interval
import com.jsdisco.musichords.data.models.Root
import com.jsdisco.musichords.domain.models.IntervalSolution
import com.jsdisco.musichords.presentation.chords.GameStatus
import com.jsdisco.musichords.presentation.common.ButtonState
import com.jsdisco.musichords.presentation.common.components.GameButton
import com.jsdisco.musichords.presentation.relativepitch.components.MusicSheetRP


@Composable
fun RelativePitchScreen(
    viewModel: RelativePitchViewModel,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    viewModel.loadSounds(LocalContext.current)

    val textGameBtns = if (viewModel.settingsRP.value.isNotationAbbr) stringArrayResource(id = R.array.intervals_list_abbr).toList() else stringArrayResource(id = R.array.intervals_list).toList()

    val chunks = 3

    fun handlePlayBtn() {
        viewModel.handlePlayBtn()
    }

    fun handleNextBtn(){
        viewModel.handleNextBtn()
    }

    fun playInterval(interval: Interval){
        viewModel.playInterval(interval)
    }

    fun checkAnswer(halfTones: Int) {
        viewModel.checkAnswer(halfTones.toString())
    }

    fun setDelay(delay: Float) {
        viewModel.setDelay(delay)
    }

    val intervalStrings = stringArrayResource(id = R.array.intervals_list)

    val title = stringResource(id = R.string.title_relative_pitch)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
        viewModel.reset()
    }

    RelativePitchScreenUI(
        viewModel.gameStatus.value,
        viewModel.activeIntervals.value,
        viewModel.buttonStates,
        viewModel.delay.value,
        ::setDelay,
        chunks,
        viewModel.solution,
        ::handlePlayBtn,
        ::handleNextBtn,
        ::checkAnswer,
        ::playInterval,
        intervalStrings,
        viewModel.roots,
        textGameBtns
    )

}

@Composable
fun RelativePitchScreenUI(
    gameStatus: GameStatus,
    intervalsList: List<Interval>,
    btnStates: List<ButtonState>,
    delay: Float,
    setDelay: (Float) -> Unit,
    chunks: Int,
    solution: IntervalSolution,
    handlePlayBtn: () -> Unit,
    handleNextBtn: () -> Unit,
    checkAnswer: (Int) -> Unit,
    playInterval: (Interval) -> Unit,
    intervalStrings: Array<String>,
    roots: List<Root>,
    textGameBtns: List<String>
) {

    val textIconNoDelay = stringResource(id = R.string.rp_icon_no_delay)
    val textIconMaxDelay = stringResource(id = R.string.rp_icon_max_delay)
    val textIconPlay = stringResource(id = R.string.rp_icon_play)
    val textIconNext = stringResource(id = R.string.rp_icon_next)


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
                    painter = painterResource(id = R.drawable.play_orange),
                    contentDescription = textIconPlay,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { handlePlayBtn() }
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = if (gameStatus == GameStatus.FOUND) R.drawable.next_orange else R.drawable.next_disabled),
                    contentDescription = textIconNext,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(enabled = gameStatus == GameStatus.FOUND) { handleNextBtn() }
                        .size(60.dp)
                )
            }

            Column {
                val solutionString = intervalStrings[solution.interval.stringIndex]
                Text(
                    text = if (gameStatus == GameStatus.FOUND) solutionString else "",
                    style = MaterialTheme.typography.h5
                )
                MusicSheetRP(roots, solution, gameStatus)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()){

            Column(modifier = Modifier.fillMaxWidth()) {
                val nestedIntervalsList = intervalsList.chunked(chunks)

                for ((i, row) in nestedIntervalsList.withIndex()) {
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
                                    val interval = row[j]
                                    val btnState = btnStates[i * chunks + j]
                                    val btnText = textGameBtns[btnState.stringIndex]
                                    GameButton(
                                        btnState = btnState,
                                        btnText = btnText,
                                        onClick = {
                                            playInterval(interval)
                                            if (gameStatus == GameStatus.GUESS) {
                                                checkAnswer(interval.halfTones)
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