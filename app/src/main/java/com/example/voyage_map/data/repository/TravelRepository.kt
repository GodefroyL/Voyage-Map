package com.example.voyage_map.data.repository

import com.example.voyage_map.data.api.RetrofitClient
import com.example.voyage_map.data.api.WikiSummaryResponse
import com.example.voyage_map.data.api.WikiSearchResponse

class TravelRepository {

    private val api = RetrofitClient.wikipediaApi

    // Search pages (used for Explore screen)
    suspend fun searchPlaces(query: String): WikiSearchResponse {
        return api.searchPages(query)
    }

    // Get details of a selected place
    suspend fun getPlaceDetails(title: String): WikiSummaryResponse {
        return api.getPageSummary(title)
    }
}
