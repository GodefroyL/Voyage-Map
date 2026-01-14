package com.example.voyage_map.data.repository

import com.example.voyage_map.data.api.GeoapifyFeature
import com.example.voyage_map.data.api.GeoapifyRetrofitClient
import java.util.Locale

class PlacesRepository {

    private val api = GeoapifyRetrofitClient.api
    private val API_KEY = "edd94f28ac0f4335a523909ded0a07f7"

    suspend fun getPlacesForCity(
        lat: Double,
        lon: Double
    ): List<GeoapifyFeature> {

        // Reduced search radius from 20km to 5km (5000m) to prevent timeouts
        val filter = String.format(Locale.US, "circle:%f,%f,5000", lon, lat)

        return api.getPlaces(
            categories = "tourism.sights",
            filter = filter,
            apiKey = API_KEY
        ).features
    }
}
