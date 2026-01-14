package com.example.voyage_map

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.voyage_map.ui.screens.CityDetailsScreen
import com.example.voyage_map.ui.screens.FavoritesScreen
import com.example.voyage_map.ui.screens.HomeScreen
import com.example.voyage_map.ui.screens.SettingsScreen
import com.example.voyage_map.ui.theme.VoyageMapTheme

// Sealed class to define all the screens in the app
sealed class Screen(val route: String, val icon: ImageVector) {
    object Home : Screen("Home", Icons.Default.Home)
    object Explore : Screen("Explore", Icons.Default.Search)
    object Favorites : Screen("Favorites", Icons.Default.Favorite)
    object Settings : Screen("Settings", Icons.Default.Settings)
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
                        Screen.Explore,
                        Screen.Favorites,
                        Screen.Settings
                    )

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.route) },
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
                composable(Screen.Explore.route) { CityDetailsScreen() }
                composable(Screen.Favorites.route) { FavoritesScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}
