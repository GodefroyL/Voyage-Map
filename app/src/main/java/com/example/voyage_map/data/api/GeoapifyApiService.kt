package com.example.voyage_map.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoapifyApiService {

    // Geocoding: Get coordinates from a city name
    @GET("v1/geocode/search")
    suspend fun geocodeCity(
        @Query("text") city: String,
        @Query("type") type: String = "city",
        @Query("limit") limit: Int = 1,
        @Query("apiKey") apiKey: String
    ): GeocodingResponse

    // Places: Get places around a given coordinate
    @GET("v2/places")
    suspend fun getPlaces(
        @Query("categories") categories: String,
        @Query("filter") filter: String,
        @Query("limit") limit: Int = 20,
        @Query("apiKey") apiKey: String
    ): GeoapifyResponse
}
