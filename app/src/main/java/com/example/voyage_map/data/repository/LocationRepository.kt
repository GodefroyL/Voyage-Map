package com.example.voyage_map.data.repository

import com.example.voyage_map.data.api.GeoapifyRetrofitClient

class LocationRepository {

    private val api = GeoapifyRetrofitClient.api
    private val API_KEY = "edd94f28ac0f4335a523909ded0a07f7"

    suspend fun getCityCoordinates(city: String): Pair<Double, Double>? {
        return try {
            val response = api.geocodeCity(city = city, apiKey = API_KEY)
            val properties = response.features.firstOrNull()?.properties
            if (properties != null) {
                Pair(properties.lat, properties.lon)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
