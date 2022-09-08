package com.jsdisco.soundscompose.presentation.home

import android.media.SoundPool
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Sound


@Composable
fun HomeScreen(
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    /*val builder: SoundPool.Builder = SoundPool.Builder().setMaxStreams(3)
    val soundPool = builder.build()

    val sounds = mutableListOf<Int>()
    listOf(R.raw.c3,R.raw.e3,R.raw.g3,R.raw.c3_vloud, R.raw.e3_vloud,R.raw.g3_vloud).forEach{
        val soundId = soundPool.load(LocalContext.current, it, 1)
        sounds.add(soundId)
    }

    fun playSounds(){
        soundPool.play(sounds[0],1f, 1f, 1, 0, 1f )
        soundPool.play(sounds[1],1f, 1f, 1, 0, 1f )
        soundPool.play(sounds[2],1f, 1f, 1, 0, 1f )
    }
    fun playSoundsLouder(){
        soundPool.play(sounds[3],1f, 1f, 1, 0, 1f )
        soundPool.play(sounds[4],1f, 1f, 1, 0, 1f )
        soundPool.play(sounds[5],1f, 1f, 1, 0, 1f )
    }*/

    val scaffoldState = rememberScaffoldState()

    val title = stringResource(id = R.string.title_home)
    LaunchedEffect(key1 = true){
        onSetAppBarTitle(title)
        onSetShowBackBtn(false)
    }
    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "HOME SCREEN")
        }
    }

}