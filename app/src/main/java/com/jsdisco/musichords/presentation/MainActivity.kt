package com.jsdisco.musichords.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jsdisco.musichords.presentation.chords.ChordsScreen
import com.jsdisco.musichords.presentation.chords.ChordsViewModel
import com.jsdisco.musichords.presentation.common.navigation.BottomNavigationBar
import com.jsdisco.musichords.presentation.common.navigation.GameScreen
import com.jsdisco.musichords.presentation.common.navigation.Screen
import com.jsdisco.musichords.presentation.chordsinfo.ChordsInfoScreen
import com.jsdisco.musichords.presentation.chordsinfo.ChordsInfoViewModel
import com.jsdisco.musichords.presentation.games.GameStartScreen
import com.jsdisco.musichords.presentation.relativepitch.RelativePitchScreen
import com.jsdisco.musichords.presentation.relativepitch.RelativePitchViewModel
import com.jsdisco.musichords.presentation.settings_chords.SettingsChordsScreen
import com.jsdisco.musichords.presentation.settings_chords_create_exercise.SettingsChordsCreateExercise
import com.jsdisco.musichords.presentation.settings_chords_exercises.SettingsChordsExercises
import com.jsdisco.musichords.presentation.settings_relative_pitch.SettingsRelativePitchScreen
import com.jsdisco.musichords.ui.theme.MusiChordsTheme
import com.jsdisco.musichords.R
import com.jsdisco.musichords.presentation.about.AboutScreen
import org.koin.androidx.compose.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MusiChordsTheme {
                val scaffoldState = rememberScaffoldState()

                val navController = rememberNavController()
                var appBarTitle by rememberSaveable{mutableStateOf("")}
                var showBackBtn by rememberSaveable{mutableStateOf(false)}

                val relativePitchViewModel = getViewModel<RelativePitchViewModel>()
                val chordsViewModel = getViewModel<ChordsViewModel>()
                val chordsInfoViewModel = getViewModel<ChordsInfoViewModel>()

                val textIconBack = stringResource(id = R.string.top_bar_icon_back)
                val textIconSettings = stringResource(id = R.string.top_bar_icon_settings)
                val textIconExercises = stringResource(id = R.string.top_bar_icon_exercises)

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = { Text(appBarTitle) },
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSecondary,
                            elevation  = 4.dp,
                            navigationIcon = if (showBackBtn) {
                                {
                                    IconButton(onClick = {
                                        when (navController.currentDestination?.route) {
                                            GameScreen.RelativePitch.route -> {
                                                navController.navigate(GameScreen.GameStart.route)
                                            }
                                            GameScreen.SettingsRelativePitch.route -> {
                                                navController.navigate(GameScreen.RelativePitch.route)
                                            }
                                            GameScreen.Chords.route -> {
                                                navController.navigate(GameScreen.GameStart.route)
                                            }
                                            GameScreen.SettingsChords.route -> {
                                                navController.navigate(GameScreen.Chords.route)
                                            }
                                            GameScreen.SettingsChordsExercises.route -> {
                                                navController.navigate(GameScreen.Chords.route)
                                            }
                                            GameScreen.SettingsChordsCreateExercise.route -> {
                                                navController.navigate(GameScreen.SettingsChordsExercises.route)
                                            }
                                            else -> {
                                                navController.navigateUp()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowBack,
                                            contentDescription = textIconBack
                                        )
                                    }
                                }
                            } else {
                                null
                            },
                            actions = {
                                if (navController.currentDestination?.route == GameScreen.Chords.route){
                                    IconButton(onClick = { navController.navigate(GameScreen.SettingsChordsExercises.route) {launchSingleTop = true} }) {
                                        Icon(
                                            imageVector = Icons.Rounded.School,
                                            contentDescription = textIconExercises,
                                            tint = MaterialTheme.colors.onSecondary)
                                    }
                                    IconButton(onClick = { navController.navigate(GameScreen.SettingsChords.route) {launchSingleTop = true} })  {
                                        Icon(
                                            imageVector = Icons.Rounded.Settings,
                                            contentDescription = textIconSettings,
                                            tint = MaterialTheme.colors.onSecondary)
                                    }
                                } else if (navController.currentDestination?.route == GameScreen.RelativePitch.route) {
                                    IconButton(onClick = { navController.navigate(GameScreen.SettingsRelativePitch.route) {launchSingleTop = true} })  {
                                        Icon(
                                            imageVector = Icons.Rounded.Settings,
                                            contentDescription = textIconSettings,
                                            tint = MaterialTheme.colors.onSecondary)
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Game.route
                    ){

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
                                    onSetAppBarTitle = {appBarTitle = it},
                                    onSetShowBackBtn = {showBackBtn = it}
                                )
                            }
                            composable(route = GameScreen.SettingsChords.route){
                                SettingsChordsScreen(
                                    viewModel = chordsViewModel,
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
                                onSetAppBarTitle = {appBarTitle = it},
                                onSetShowBackBtn = {showBackBtn = it}
                            )
                        }

                        composable(route = Screen.About.route){
                            AboutScreen(
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
