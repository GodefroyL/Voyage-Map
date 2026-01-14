package com.example.voyage_map.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.viewmodel.CityDetailsViewModel

// Renamed for clarity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(
    cityDetailsViewModel: CityDetailsViewModel = viewModel()
) {
    // Correctly collect state from the viewModel instance
    val places by cityDetailsViewModel.places.collectAsState()
    val isLoading by cityDetailsViewModel.isLoading.collectAsState()
    val error by cityDetailsViewModel.error.collectAsState()

    var query by remember { mutableStateOf("Paris") } // Default value for easier testing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search a city") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            // Correctly call the method on the viewModel instance
            onClick = { cityDetailsViewModel.loadCityDetails(query) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Search for Places")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Handle the 3 possible states: loading, error, or success
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "An unknown error occurred.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(places) { feature ->
                        PlaceItem(feature) // Pass the correct object type
                    }
                }
            }
        }
    }
}

// Updated to accept a GeoapifyFeature
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceItem(feature: GeoapifyFeature) {
    val properties = feature.properties

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Display the name of the place, if it exists
            properties.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Display the full address, if it exists
            properties.fullAddress?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
