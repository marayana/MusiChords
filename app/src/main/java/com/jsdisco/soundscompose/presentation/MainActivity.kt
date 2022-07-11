package com.jsdisco.soundscompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jsdisco.soundscompose.presentation.common.navigation.BottomNavigationBar
import com.jsdisco.soundscompose.presentation.common.navigation.GameScreen
import com.jsdisco.soundscompose.presentation.common.navigation.Screen
import com.jsdisco.soundscompose.presentation.chords.ChordsScreen
import com.jsdisco.soundscompose.presentation.chords.ChordsViewModel
import com.jsdisco.soundscompose.presentation.chordsinfo.ChordsInfoScreen
import com.jsdisco.soundscompose.presentation.chordsinfo.ChordsInfoViewModel
import com.jsdisco.soundscompose.presentation.common.components.SettingsDrawer
import com.jsdisco.soundscompose.presentation.games.GameStartScreen
import com.jsdisco.soundscompose.presentation.home.HomeScreen
import com.jsdisco.soundscompose.presentation.relativepitch.RelativePitchViewModel
import com.jsdisco.soundscompose.ui.theme.SoundsComposeTheme
import com.jsdisco.soundscompose.presentation.relativepitch.RelativePitchScreen
import com.jsdisco.soundscompose.ui.theme.DTMenuBackground
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            //val homeViewModel: HomeViewModel = hiltViewModel()
            val relativePitchViewModel: RelativePitchViewModel = hiltViewModel()
            val chordsViewModel: ChordsViewModel = hiltViewModel()
            val chordsInfoViewModel: ChordsInfoViewModel = hiltViewModel()

            SoundsComposeTheme {
                val navController = rememberNavController()
                var appBarTitle by rememberSaveable{mutableStateOf("")}
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        val scope = rememberCoroutineScope()
                        TopAppBar(
                            title = { Text(appBarTitle) },
                            backgroundColor = DTMenuBackground,
                            contentColor = Color(0xBBffffff),
                            elevation  = 4.dp,
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { scaffoldState.drawerState.open() }
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Settings,
                                        contentDescription = "open settings"
                                    )
                                }
                            }
                        )
                    },
                    drawerContent = { SettingsDrawer() },
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Game.route
                    ){

                        composable(route = Screen.Home.route){
                            HomeScreen(
                                onSetAppBarTitle = {appBarTitle = it}
                            )
                        }

                        navigation(route = Screen.Game.route, startDestination = GameScreen.GameStart.route){
                            composable(route = GameScreen.GameStart.route){
                                GameStartScreen(
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it}
                                )
                            }
                            composable(route = GameScreen.RelativePitch.route){
                                RelativePitchScreen(
                                    viewModel = relativePitchViewModel,
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it}
                                )
                            }
                            composable(route = GameScreen.Chords.route){
                                ChordsScreen(
                                    viewModel = chordsViewModel,
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it}
                                )
                            }
                        }

                        composable(route = Screen.ChordsInfo.route){
                            ChordsInfoScreen(
                                viewModel = chordsInfoViewModel,
                                navController = navController,
                                onSetAppBarTitle = {appBarTitle = it}
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SoundsComposeTheme {

    }
}

/**
 *
 * TODO: ViewModel problems
 * TODO: settings
 * TODO: rp: allow intervals > 1 octave
 * TODO: layout small screens
 *
 * */