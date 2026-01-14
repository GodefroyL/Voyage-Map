package com.example.voyage_map.data.repository

import com.example.voyage_map.data.api.RetrofitClient
import com.example.voyage_map.data.api.WikiSearchResponse
import com.example.voyage_map.data.api.WikiSummaryResponse

class TravelRepository {

    private val api = RetrofitClient.wikipediaApi

    // 🔍 Search cities / places
    suspend fun searchPlaces(query: String): WikiSearchResponse {
        return api.searchPages(query = query)
    }

    // 📄 Page details
    suspend fun getPlaceDetails(title: String): WikiSummaryResponse {
        return api.getPageSummary(title)
    }
}
