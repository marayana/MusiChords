package com.jsdisco.soundscompose.presentation.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Hearing
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Piano

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            label = "home",
            icon = Icons.Rounded.Home,
            routes = listOf(Screen.Home.route)
        ),
        BottomNavItem(
            label = "training",
            //icon = Icons.Rounded.Games,
            icon = Icons.Rounded.Hearing,
            routes = listOf(
                GameScreen.GameStart.route,
                GameScreen.RelativePitch.route,
                GameScreen.Chords.route,
                GameScreen.SettingsRelativePitch.route,
                GameScreen.SettingsChords.route,
                GameScreen.SettingsChordsCreateExercise.route,
                GameScreen.SettingsChordsExercises.route
            )
        ),
        BottomNavItem(
            label = "chords",
            icon = Icons.Rounded.Piano,
            routes = listOf(Screen.ChordsInfo.route)
        )
    )
}