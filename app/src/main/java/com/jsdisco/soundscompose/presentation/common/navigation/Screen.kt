package com.jsdisco.soundscompose.presentation.common.navigation

sealed class Screen(val route: String){
    object Home: Screen("home")
    object Game: Screen("games")
    object ChordsInfo: Screen("chordsinfo")
}

sealed class GameScreen(val route: String){
    object GameStart: GameScreen("games/start")
    object RelativePitch: GameScreen("games/relativepitch")
    object Chords: GameScreen("games/chords")
}
