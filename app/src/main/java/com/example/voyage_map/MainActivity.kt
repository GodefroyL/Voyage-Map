package com.example.voyage_map

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voyage_map.ui.theme.VoyageMapTheme
import com.example.voyage_map.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()

            VoyageMapTheme(
                darkTheme = themeViewModel.isDarkTheme.value
            ) {
                VoyageMapApp()
            }
        }
    }
}
