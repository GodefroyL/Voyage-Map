package com.example.voyage_map.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voyage_map.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen()
        }
        composable(NavRoutes.Explore.route) {
            ExploreScreen()
        }
        composable(NavRoutes.Favorites.route) {
            FavoritesScreen()
        }
        composable(NavRoutes.Settings.route) {
            SettingsScreen()
        }
    }
}
