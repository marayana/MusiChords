package com.jsdisco.soundscompose.presentation.relativepitch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Interval
import com.jsdisco.soundscompose.presentation.common.components.ButtonState


@Composable
fun RelativePitchScreen(
    viewModel: RelativePitchViewModel,
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit
) {

    viewModel.loadSounds(LocalContext.current)


    fun newRoundAndPlay() {
        viewModel.newRoundAndPlay()
    }

    fun checkAnswer(halfTones: Int) {
        viewModel.checkAnswer(halfTones.toString())
    }

    fun setDelay(delay: Float){
        viewModel.setDelay(delay)
    }


    LaunchedEffect(Unit){
        onSetAppBarTitle("Relative Pitch")
        viewModel.reset()
    }

    RelativePitchScreenUI(
        viewModel.intervals,
        viewModel.buttonStates,
        viewModel.delay.value,
        ::setDelay,
        ::newRoundAndPlay,
        ::checkAnswer)

}

@Composable
fun RelativePitchScreenUI(
    intervalsList: List<Interval>,
    btnStates: List<ButtonState>,
    delay: Float,
    setDelay: (Float) -> Unit,
    newRoundAndPlay: () -> Unit,
    checkAnswer: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Slider(
            value = delay,
            onValueChange = {value -> setDelay(value)},
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Image(
            painter = painterResource(id = R.drawable.play_orange),
            contentDescription = "play sound",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { newRoundAndPlay() }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {

                val nestedIntervalsList = intervalsList.chunked(3)

                for (row in nestedIntervalsList) {
                    Row(modifier = Modifier
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
                                    .fillMaxWidth()
                                    .height(60.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = btnState.bgColour),
                                    onClick = { if (!btnState.isDisabled) { checkAnswer(interval.halfTones) } }
                                ) {
                                    Text(
                                        text = btnState.text, color = btnState.textColour, textAlign = TextAlign.Center
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