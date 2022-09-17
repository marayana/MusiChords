package com.jsdisco.musichords.presentation.common.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val routes: List<String>
)