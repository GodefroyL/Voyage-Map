package com.example.voyage_map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.data.model.PlaceUiModel
import com.example.voyage_map.data.repository.LocationRepository
import com.example.voyage_map.data.repository.PlacesRepository
import com.example.voyage_map.utils.score
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val locationRepository = LocationRepository()
    private val placesRepository = PlacesRepository()

    // Holds the private, mutable state for the list of places.
    private val _places = MutableStateFlow<List<PlaceUiModel>>(emptyList())
    // Exposes a public, read-only flow for the UI to observe changes.
    val places: StateFlow<List<PlaceUiModel>> = _places.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Fetches coordinates for a city and then loads the nearby places.
     */
    // In app/src/main/java/com/example/voyage_map/viewmodel/HomeViewModel.kt

    fun loadCityDetails(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _places.value = emptyList()

            try {
                // Step 1: Get coordinates for the given city name.
                val coordinates = locationRepository.getCityCoordinates(cityName)

                if (coordinates != null) {
                    val (lat, lon) = coordinates
                    // Step 2: Fetch places. This already returns List<PlaceUiModel>.
                    val fetchedPlaces = placesRepository.getPlacesForCity(lat, lon)

                    // Step 3: Simply sort the already-mapped list and update the state.
                    _places.value = fetchedPlaces
                        .sortedByDescending { it.score } // The .map call has been removed.

                } else {
                    _error.value = "Could not find coordinates for '$cityName'."
                }
            } catch (e: Exception) {
                _error.value = "An error occurred: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    /**
     * Toggles the 'isLiked' state for a specific place.
     */
    fun toggleLike(placeId: String) {
        // 'update' is a thread-safe way to modify the StateFlow's value.
        _places.update { currentPlaces ->
            // 'map' creates a new list with the updated item.
            currentPlaces.map { place ->
                if (place.id == placeId) {
                    // If this is the place, create a new object with the 'isLiked' value flipped.
                    place.copy(isLiked = !place.isLiked)
                } else {
                    // Otherwise, return the same object.
                    place
                }
            }
        }
    }

    /**
     * A private mapper function to convert the API model (GeoapifyFeature)
     * to the UI model (PlaceUiModel). This separates data layer from UI layer concerns.
     */
    // ... inside HomeViewModel class

    private fun GeoapifyFeature.toPlaceUiModel(): PlaceUiModel {
        return PlaceUiModel(
            // Use the corrected property name 'placeId'
            id = this.properties.placeId,

            name = this.properties.name ?: "Unknown Place",
            address = this.properties.fullAddress,
            categories = this.properties.categories,
            wikipediaUrl = this.properties.wikiAndMedia?.wikipedia,
            isLiked = false,
            score = this.score()
        )
    }
}
