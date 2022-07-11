package com.jsdisco.soundscompose.presentation.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Piano

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Rounded.Home,
            routes = listOf("home")
        ),
        BottomNavItem(
            label = "Games",
            icon = Icons.Rounded.Games,
            routes = listOf("games/start", "games/relativepitch", "games/chords")
        ),
        BottomNavItem(
            label = "Chords",
            icon = Icons.Rounded.Piano,
            routes = listOf("chordsinfo")
        )
    )
}