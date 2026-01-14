package com.example.voyage_map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.data.repository.LocationRepository
import com.example.voyage_map.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CityDetailsViewModel : ViewModel() {

    // Instantiate both repositories needed for the full flow
    private val locationRepository = LocationRepository()
    private val placesRepository = PlacesRepository()

    // State for the list of places
    private val _places = MutableStateFlow<List<GeoapifyFeature>>(emptyList())
    val places: StateFlow<List<GeoapifyFeature>> = _places

    // State for loading indicator, useful for the UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State for error messages, also for the UI
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Loads all details for a city by orchestrating the 2-step process:
     * 1. Geocode the city name to get coordinates.
     * 2. Fetch places of interest around those coordinates.
     */
    fun loadCityDetails(cityName: String) {
        viewModelScope.launch {
            // Reset state at the beginning of a new load
            _isLoading.value = true
            _error.value = null
            _places.value = emptyList()

            try {
                // Step 1: Get coordinates for the city name
                val coordinates = locationRepository.getCityCoordinates(cityName)

                if (coordinates != null) {
                    val (lat, lon) = coordinates
                    // Step 2: If coordinates are found, get places for them
                    _places.value = placesRepository.getPlacesForCity(lat, lon)
                } else {
                    // Handle the case where the city could not be geocoded
                    _error.value = "Could not find coordinates for '$cityName'."
                }
            } catch (e: Exception) {
                // Handle any exceptions during the process (e.g., network errors)
                _error.value = "An error occurred: ${e.message}"
                e.printStackTrace() // Log the stack trace for debugging
            } finally {
                // Ensure loading is set to false once the process is complete
                _isLoading.value = false
            }
        }
    }
}
