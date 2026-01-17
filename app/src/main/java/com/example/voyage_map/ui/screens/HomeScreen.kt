package com.example.voyage_map.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voyage_map.data.model.PlaceUiModel
import com.example.voyage_map.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cityDetailsViewModel: HomeViewModel = viewModel()
) {
    // State from the ViewModel is the single source of truth
    val places by cityDetailsViewModel.places.collectAsState()
    val isLoading by cityDetailsViewModel.isLoading.collectAsState()
    val error by cityDetailsViewModel.error.collectAsState()

    // UI-specific state that doesn't need to live in the ViewModel
    var query by remember { mutableStateOf("") }
    var hasSearched by remember { mutableStateOf(false) }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

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

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search for a city") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (query.isNotBlank()) {
                        hasSearched = true
                        cityDetailsViewModel.loadCityDetails(query)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Search for places")
            }

            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> LoadingShimmerEffect()

                error != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        error ?: "An unknown error occurred.",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                !hasSearched -> PopularDestinations { city ->
                    query = city
                    hasSearched = true
                    cityDetailsViewModel.loadCityDetails(city)
                }

                places.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No places found for \"$query\".",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    val topPlaces = places.take(5)
                    val otherPlaces = places.drop(5)

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        item {
                            Text(
                                "Top Places",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(topPlaces) { place ->
                            PlaceItem(
                                place = place,
                                isTopPlace = true,
                                onLikeClick = {
                                    // The UI sends an event to the ViewModel
                                    cityDetailsViewModel.toggleLike(place.id)
                                }
                            )
                        }

                        if (otherPlaces.isNotEmpty()) {
                            item {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "More Places",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(otherPlaces) { place ->
                                PlaceItem(
                                    place = place,
                                    isTopPlace = false,
                                    onLikeClick = {
                                        // The UI sends an event to the ViewModel
                                        cityDetailsViewModel.toggleLike(place.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopularDestinations(onClick: (String) -> Unit) {
    val cities = listOf("Paris", "London", "Tokyo", "New York", "Rome")

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text(
                "Or try one of our popular destinations:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        items(cities) { city ->
            DestinationCard(city = city) { onClick(city) }
        }
    }
}

@Composable
fun PlaceItem(
    place: PlaceUiModel,
    isTopPlace: Boolean,
    onLikeClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(if (isTopPlace) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTopPlace)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = getIconForCategory(place.categories?.firstOrNull()),
                contentDescription = null,
                modifier = Modifier.size(40.dp).padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(Modifier.weight(1f)) {
                Text(place.name, fontWeight = FontWeight.Bold)
                place.address?.let {
                    Text(it, color = Color.Gray)
                }
            }

            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (place.isLiked)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (place.isLiked) Color.Red else Color.Gray
                )
            }

            place.wikipediaUrl?.let { url ->
                IconButton(
                    onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        )
                    }
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Wikipedia")
                }
            }
        }
    }
}

@Composable
fun DestinationCard(city: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            city,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingShimmerEffect() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(6) { PlaceItemPlaceholder() }
    }
}

@Composable
fun PlaceItemPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shimmerBackground()
    )
}

fun Modifier.shimmerBackground(
    shape: Shape = RoundedCornerShape(8.dp)
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        ),
        label = "shimmer_translate_animation"
    )

    background(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.9f),
                Color.LightGray.copy(alpha = 0.4f),
                Color.LightGray.copy(alpha = 0.9f)
            ),
            start = Offset.Zero,
            end = Offset(translate, translate)
        ),
        shape = shape
    )
}

fun getIconForCategory(category: String?): ImageVector =
    when {
        category?.contains("museum") == true -> Icons.Default.AccountBalance
        category?.contains("heritage") == true -> Icons.Default.Place
        category?.contains("attraction") == true -> Icons.Default.Landscape
        else -> Icons.Default.Place
    }
