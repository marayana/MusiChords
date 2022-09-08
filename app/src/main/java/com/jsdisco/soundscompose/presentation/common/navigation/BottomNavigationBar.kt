package com.jsdisco.soundscompose.presentation.common.navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsdisco.soundscompose.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.secondary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route

        val navLabels = stringArrayResource(id = R.array.bottom_nav)

        BottomNavItems.items.forEachIndexed{ i, navItem ->
            BottomNavigationItem(
                selected = navItem.routes.contains(currentRoute),
                onClick = { navController.navigate(navItem.routes[0]) },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label)},
                label = { Text(text = navLabels[i]) }
            )
        }
    }
}