package com.example.voyage_map.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.viewmodel.CityDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(
    cityDetailsViewModel: CityDetailsViewModel = viewModel()
) {
    val places by cityDetailsViewModel.places.collectAsState()
    val isLoading by cityDetailsViewModel.isLoading.collectAsState()
    val error by cityDetailsViewModel.error.collectAsState()
    var query by remember { mutableStateOf("Paris") }

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
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Enter a city name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { cityDetailsViewModel.loadCityDetails(query) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Search for Places")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    LoadingShimmerEffect()
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = error ?: "An unknown error occurred.",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                places.isEmpty() && !isLoading -> {
                     Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Search for a city to discover amazing places!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    val topPlaces = places.take(5)
                    val otherPlaces = places.drop(5)

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            Text("Top Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }
                        items(topPlaces) { feature ->
                            PlaceItem(feature, isTopPlace = true)
                        }

                        if (otherPlaces.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("More Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            items(otherPlaces) { feature ->
                                PlaceItem(feature, isTopPlace = false)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.shimmerBackground(shape: Shape = RoundedCornerShape(4.dp)): Modifier = composed {
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.4f),
            Color.LightGray.copy(alpha = 0.9f)
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    background(brush, shape)
}

@Composable
fun LoadingShimmerEffect() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Top Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(5) { PlaceItemPlaceholder() }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { Text("More Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(5) { PlaceItemPlaceholder(isTopPlace = false) }
    }
}

@Composable
fun PlaceItemPlaceholder(isTopPlace: Boolean = true) {
    val backgroundColor = if(isTopPlace) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(if (isTopPlace) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).shimmerBackground(RoundedCornerShape(8.dp)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(20.dp).shimmerBackground())
                Box(modifier = Modifier.fillMaxWidth(0.9f).height(16.dp).shimmerBackground())
            }
        }
    }
}

@Composable
fun PlaceItem(feature: GeoapifyFeature, isTopPlace: Boolean) {
    val properties = feature.properties
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(if (isTopPlace) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isTopPlace) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = getIconForCategory(properties.categories?.firstOrNull()),
                contentDescription = "Category",
                modifier = Modifier.size(40.dp).padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                properties.name?.let {
                    Text(text = it, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                properties.fullAddress?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
            properties.wikiAndMedia?.wikipedia?.let { wikiUrl ->
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Info, contentDescription = "Open Wikipedia page")
                }
            }
        }
    }
}

fun getIconForCategory(category: String?): ImageVector {
    return when {
        category?.contains("museum") == true -> Icons.Default.AccountBalance
        category?.contains("heritage") == true -> Icons.Default.Place
        category?.contains("attraction") == true -> Icons.Default.Landscape
        else -> Icons.Default.Place
    }
}
