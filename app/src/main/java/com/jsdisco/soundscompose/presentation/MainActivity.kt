package com.jsdisco.soundscompose.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.School
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jsdisco.soundscompose.presentation.chords.ChordsScreen
import com.jsdisco.soundscompose.presentation.chords.ChordsViewModel
import com.jsdisco.soundscompose.presentation.common.navigation.BottomNavigationBar
import com.jsdisco.soundscompose.presentation.common.navigation.GameScreen
import com.jsdisco.soundscompose.presentation.common.navigation.Screen
import com.jsdisco.soundscompose.presentation.chordsinfo.ChordsInfoScreen
import com.jsdisco.soundscompose.presentation.chordsinfo.ChordsInfoViewModel
import com.jsdisco.soundscompose.presentation.common.components.SettingsDrawer
import com.jsdisco.soundscompose.presentation.games.GameStartScreen
import com.jsdisco.soundscompose.presentation.home.HomeScreen
import com.jsdisco.soundscompose.presentation.relativepitch.RelativePitchScreen
import com.jsdisco.soundscompose.presentation.relativepitch.RelativePitchViewModel
import com.jsdisco.soundscompose.presentation.settings_chords.SettingsChordsScreen
import com.jsdisco.soundscompose.presentation.settings_chords_create_exercise.SettingsChordsCreateExercise
import com.jsdisco.soundscompose.presentation.settings_chords_exercises.SettingsChordsExercises
import com.jsdisco.soundscompose.presentation.settings_relative_pitch.SettingsRelativePitchScreen
import com.jsdisco.soundscompose.ui.theme.SoundsComposeTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SoundsComposeTheme {
                val scaffoldState = rememberScaffoldState()

                val navController = rememberNavController()
                var appBarTitle by rememberSaveable{mutableStateOf("")}
                var showBackBtn by rememberSaveable{mutableStateOf(false)}

                //val homeViewModel = getViewModel<HomeViewModel>()
                val relativePitchViewModel = getViewModel<RelativePitchViewModel>()
                val chordsViewModel = getViewModel<ChordsViewModel>()
                val chordsInfoViewModel = getViewModel<ChordsInfoViewModel>()


                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        //val scope = rememberCoroutineScope()
                        TopAppBar(
                            title = { Text(appBarTitle) },
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSecondary,
                            elevation  = 4.dp,
                            navigationIcon = if (showBackBtn) {
                                {
                                    IconButton(onClick = {
                                        //scope.launch { scaffoldState.drawerState.open() }
                                        navController.navigateUp()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowBack,
                                            contentDescription = "open settings"
                                        )
                                    }
                                }
                            } else {
                                null
                            },
                            actions = {
                                if (navController.currentDestination?.route == GameScreen.Chords.route){
                                    IconButton(onClick = { navController.navigate(GameScreen.SettingsChordsExercises.route) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.School,
                                            contentDescription = "open exercises", /* TODO */
                                            tint = MaterialTheme.colors.onSecondary)
                                    }
                                    IconButton(onClick = { navController.navigate(GameScreen.SettingsChords.route) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Settings,
                                            contentDescription = "open settings", /* TODO */
                                            tint = MaterialTheme.colors.onSecondary)
                                    }
                                }
                            }
                        )
                    },
                    //drawerContent = { SettingsDrawer() },
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
                                onSetAppBarTitle = {appBarTitle = it},
                                onSetShowBackBtn = {showBackBtn = it}
                            )
                        }

                        navigation(route = Screen.Game.route, startDestination = GameScreen.GameStart.route){
                            composable(route = GameScreen.GameStart.route){
                                GameStartScreen(
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.RelativePitch.route){
                                RelativePitchScreen(
                                    viewModel = relativePitchViewModel,
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.SettingsRelativePitch.route){
                                SettingsRelativePitchScreen(
                                    viewModel = relativePitchViewModel,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.Chords.route){
                                ChordsScreen(
                                    viewModel = chordsViewModel,
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.SettingsChords.route){
                                SettingsChordsScreen(
                                    viewModel = chordsViewModel,
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.SettingsChordsCreateExercise.route){
                                SettingsChordsCreateExercise(
                                    viewModel = chordsViewModel,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.SettingsChordsExercises.route){
                                SettingsChordsExercises(
                                    viewModel = chordsViewModel,
                                    navController = navController,
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                        }

                        composable(route = Screen.ChordsInfo.route){
                            ChordsInfoScreen(
                                viewModel = chordsInfoViewModel,
                                navController = navController,
                                onSetAppBarTitle = {appBarTitle = it},
                                onSetShowBackBtn = {showBackBtn = it}
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

 *
 * TODO: string resources (GameButton / ChordsInfo) and throughout the app (todos)
 * TODO: chordsInfo: result language, chords settings: exercise language
 * TODO: Settings rp: intervals > 1 octave y/n, notation (halftones / intervals (3b...) / ...?)
 * TODO: create new exercise: save button style
 * TODO: create exercise: Icons on Toasts / snackbar behind content / maybe try alertDialog instead
 * TODO: When navigating to the same route twice, it takes two clicks on back button to go back
 *
 *
 * FUTURE: kick Home Screen and add Statistics Screen
 * FUTURE: third game: select random chord and pick keys on piano
 * FUTURE: find a way to add sheet to chords info for compact screen sizes
 * FUTURE: add more chords
 *
 * I don't think that buymeacoffee is a good addition to the app
 *
 *
 *
 *
 * */