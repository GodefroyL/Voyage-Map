package com.example.voyage_map.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.voyage_map.ui.theme.VoyageMapTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    VoyageMapTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Voyage Map") },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "Welcome!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Explore the world with ease",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search for a city") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = true // This field is for display only on the Home Screen
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "Popular Destinations",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // The list of destinations can be dynamic in a real app
                val popularDestinations = listOf("Paris", "London", "Tokyo", "New York", "Rome")

                items(popularDestinations.size) { index ->
                    DestinationCard(popularDestinations[index])
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationCard(city: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = city,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
