package com.jsdisco.musichords.presentation.common.navigation

sealed class Screen(val route: String){
    object Game: Screen("games")
    object ChordsInfo: Screen("chordsinfo")
}

sealed class GameScreen(val route: String){
    object GameStart: GameScreen("games/start")
    object RelativePitch: GameScreen("games/relativepitch")
    object Chords: GameScreen("games/chords")
    object SettingsRelativePitch: GameScreen("games/settingsrelativepitch")
    object SettingsChords: GameScreen("games/settingschords")
    object SettingsChordsCreateExercise: GameScreen("games/settingschordscreateexercise")
    object SettingsChordsExercises: GameScreen("games/settingschordsexercises")
}
