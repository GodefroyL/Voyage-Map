package com.example.voyage_map.data.repository

import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.data.api.GeoapifyRetrofitClient

// Renamed class to match the file name for consistency
class PlacesRepository {

    private val api = GeoapifyRetrofitClient.api
    // IMPORTANT: Replace "YOUR_API_KEY" with your actual Geoapify API key
    private val API_KEY = "YOUR_API_KEY"

    suspend fun getPlacesForCity(
        lat: Double,
        lon: Double
    ): List<GeoapifyFeature> {

        val filter = "circle:$lon,$lat,20000" // 20 km around the center of the city

        // The call is correct, it returns the list of features from the response
        return api.getPlaces(
            categories = "tourism.attraction,tourism.sights,tourism.museum",
            filter = filter,
            apiKey = API_KEY
        ).features
    }
}
