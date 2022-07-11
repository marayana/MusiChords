package com.jsdisco.soundscompose.presentation.chords

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Chord
import com.jsdisco.soundscompose.presentation.common.components.ButtonState


@Composable
fun ChordsScreen(
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit,
    viewModel: ChordsViewModel
) {

    viewModel.loadSounds(LocalContext.current)




    fun newRoundAndPlay() {
        viewModel.newRoundAndPlay()
    }

    fun checkAnswer(chordName: String){
        viewModel.checkAnswer(chordName)
    }

    fun setDelay(delay: Float){
        viewModel.setDelay(delay)
    }

    LaunchedEffect(Unit){
        onSetAppBarTitle("Chords")
        viewModel.reset()
    }
    ChordsScreenUI(
        viewModel.chords,
        viewModel.buttonStates,
        viewModel.delay.value,
        ::setDelay,
        ::newRoundAndPlay,
        ::checkAnswer
    )
}

@Composable
fun ChordsScreenUI(
    chordsList: List<Chord>,
    btnStates: List<ButtonState>,
    delay: Float,
    setDelay: (Float) -> Unit,
    newRoundAndPlay: () -> Unit,
    checkAnswer: (String) -> Unit
){
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
            painter = painterResource(id = R.drawable.play_pink),
            contentDescription = "play sound",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { newRoundAndPlay() }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {

                val nestedChordsList = chordsList.chunked(3)

                for ((i,row) in nestedChordsList.withIndex()) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for ((j,chord) in row.withIndex()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            ) {
                                val btnState = btnStates[i * 3 + j]
                                Button(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),

                                    border = BorderStroke(2.dp, btnState.borderColour),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = btnState.bgColour,
                                        disabledBackgroundColor = btnState.bgColour
                                    ),
                                    enabled = !btnState.isDisabled,
                                    onClick = { checkAnswer(chord.name) },
                                ) {
                                    Text(
                                        text = chord.name, color = btnState.textColour, textAlign = TextAlign.Center
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