package com.example.voyage_map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.data.repository.LocationRepository
import com.example.voyage_map.data.repository.PlacesRepository
import com.example.voyage_map.utils.score
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val locationRepository = LocationRepository()
    private val placesRepository = PlacesRepository()

    private val _places = MutableStateFlow<List<GeoapifyFeature>>(emptyList())
    val places: StateFlow<List<GeoapifyFeature>> = _places

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCityDetails(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _places.value = emptyList()

            try {
                val coordinates = locationRepository.getCityCoordinates(cityName)

                if (coordinates != null) {
                    val (lat, lon) = coordinates
                    val fetchedPlaces = placesRepository.getPlacesForCity(lat, lon)

                    _places.value = fetchedPlaces.sortedByDescending { it.score() }

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
}
