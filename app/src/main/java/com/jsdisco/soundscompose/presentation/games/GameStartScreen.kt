package com.jsdisco.soundscompose.presentation.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jsdisco.soundscompose.presentation.common.navigation.GameScreen
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.ui.theme.*

@Composable
fun GameStartScreen(
    navController: NavController,
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    val title = stringResource(id = R.string.title_games)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier
            .width(300.dp)
            .height(120.dp)
            .clickable { navController.navigate(GameScreen.RelativePitch.route) }
            .clip(shape = RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Orange,
                        OrangeLight
                    )
                )
            ))
        {
            Text(
                text = stringResource(id = R.string.title_relative_pitch),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(120.dp)
                .clickable { navController.navigate(GameScreen.Chords.route) }
                .clip(shape = RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Pink,
                            PinkLight
                        )
                    )
                ))
        {
            Text(
                text = stringResource(id = R.string.title_chords),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}