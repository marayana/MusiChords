package com.jsdisco.soundscompose.presentation.relativepitch

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Interval
import com.jsdisco.soundscompose.presentation.common.ButtonState
import com.jsdisco.soundscompose.presentation.common.navigation.GameScreen
import kotlinx.coroutines.launch


@Composable
fun RelativePitchScreen(
    viewModel: RelativePitchViewModel,
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    viewModel.loadSounds(LocalContext.current)

    val allSoundsLoaded by rememberSaveable {
        mutableStateOf(viewModel.allSoundsLoaded)
    }

    fun newRoundAndPlay() {
        viewModel.newRoundAndPlay()
    }

    fun checkAnswer(halfTones: Int) {
        viewModel.checkAnswer(halfTones.toString())
    }

    fun setDelay(delay: Float) {
        viewModel.setDelay(delay)
    }

    fun navToSettings() {
        navController.navigate(GameScreen.SettingsRelativePitch.route)
    }

    val title = stringResource(id = R.string.title_relative_pitch)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(true)
        viewModel.reset()
    }

    RelativePitchScreenUI(
        allSoundsLoaded.value,
        viewModel.intervals,
        viewModel.buttonStates,
        viewModel.delay.value,
        ::setDelay,
        ::newRoundAndPlay,
        ::checkAnswer,
        ::navToSettings
    )

}

@Composable
fun RelativePitchScreenUI(
    allSoundsLoaded: Boolean,
    intervalsList: List<Interval>,
    btnStates: List<ButtonState>,
    delay: Float,
    setDelay: (Float) -> Unit,
    newRoundAndPlay: () -> Unit,
    checkAnswer: (Int) -> Unit,
    navToSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Slider(
                value = delay,
                onValueChange = { value -> setDelay(value) },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            IconButton(onClick = {
                navToSettings()
            }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "open settings"
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.play_orange),
            contentDescription = "play sound",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                    if (allSoundsLoaded) {
                        newRoundAndPlay()
                    }
                }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {

                val nestedIntervalsList = intervalsList.chunked(3)

                for (row in nestedIntervalsList) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (interval in row) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            ) {
                                val btnState = btnStates[interval.halfTones - 1]
                                Button(modifier = Modifier
                                    .fillMaxWidth(),
                                    border = BorderStroke(2.dp, btnState.borderColour),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.secondary,
                                    ),
                                    enabled = btnState.isEnabled,
                                    onClick = {
                                        checkAnswer(interval.halfTones)
                                    }
                                ) {
                                    Text(
                                        text = stringArrayResource(R.array.intervals)[interval.halfTones-1],
                                        textAlign = TextAlign.Center
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