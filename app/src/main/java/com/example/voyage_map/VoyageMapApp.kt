package com.example.voyage_map

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.voyage_map.ui.theme.VoyageMapTheme
import com.example.voyage_map.R
import com.example.voyage_map.ui.screens.FavoritesScreen
import com.example.voyage_map.ui.screens.HomeScreen
import com.example.voyage_map.ui.screens.SettingsScreen

sealed class Screen(val route: String, val labelResId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.screen_home, Icons.Default.Home)
    object Favorites : Screen("favorites", R.string.screen_favorites, Icons.Default.Favorite)
    object Settings : Screen("settings", R.string.screen_settings, Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoyageMapApp() {
    VoyageMapTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    val items = listOf(
                        Screen.Home,
                        Screen.Favorites,
                        Screen.Settings
                    )

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.labelResId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { HomeScreen() }
                composable(Screen.Favorites.route) { FavoritesScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}
