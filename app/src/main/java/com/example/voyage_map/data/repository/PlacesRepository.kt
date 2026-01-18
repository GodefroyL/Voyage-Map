package com.example.voyage_map.data.repository

import com.example.voyage_map.data.api.GeoapifyRetrofitClient
import java.util.Locale
import com.example.voyage_map.data.model.PlaceUiModel
import com.example.voyage_map.utils.score // 1. Import the score function

class PlacesRepository {

    private val api = GeoapifyRetrofitClient.api
    private val API_KEY = "edd94f28ac0f4335a523909ded0a07f7"

    suspend fun getPlacesForCity(
        lat: Double,
        lon: Double
    ): List<PlaceUiModel> {

        val filter = String.format(Locale.US, "circle:%f,%f,5000", lon, lat)

        return api.getPlaces(
            categories = "tourism.sights",
            filter = filter,
            apiKey = API_KEY
        ).features.map { feature ->
            PlaceUiModel(
                id = "${feature.geometry.coordinates[0]}_${feature.geometry.coordinates[1]}",
                name = feature.properties.name ?: "Unknown place",
                address = feature.properties.fullAddress,
                categories = feature.properties.categories,
                wikipediaUrl = feature.properties.wikiAndMedia?.wikipedia,
                score = feature.score() // 2. Add the score parameter here
            )
        }
    }
}
