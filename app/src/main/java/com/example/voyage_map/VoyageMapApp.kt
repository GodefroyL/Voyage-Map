package com.example.voyage_map

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.voyage_map.navigation.BottomBar
import com.example.voyage_map.navigation.NavGraph
import com.example.voyage_map.ui.theme.VoyageMapTheme

@Composable
fun VoyageMapApp() {
    VoyageMapTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomBar(navController)
            }
        ) { padding ->
            NavGraph(navController = navController)
        }
    }
}
