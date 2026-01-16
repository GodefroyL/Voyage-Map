package com.example.voyage_map.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Favorites : NavRoutes("favorites")
    object Settings : NavRoutes("settings")
}
