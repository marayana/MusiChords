package com.jsdisco.soundscompose.presentation.common.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsdisco.soundscompose.ui.theme.DTMenuBackground

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation(
        backgroundColor = DTMenuBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItems.items.forEach{ navItem ->
            BottomNavigationItem(
                selected = navItem.routes.contains(currentRoute),
                onClick = { navController.navigate(navItem.routes[0]) },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label)},
                label = { Text(text = navItem.label) }
            )
        }
    }
}