package com.example.voyage_map.data.api

import com.google.gson.annotations.SerializedName

/**
 * Represents the root of the Geoapify Places API response.
 * It's a GeoJSON FeatureCollection.
 */
data class GeoapifyResponse(
    val features: List<GeoapifyFeature>
)

/**
 * Represents a single GeoJSON Feature, which corresponds to a place.
 */
data class GeoapifyFeature(
    val properties: GeoapifyProperties
)

/**
 * Contains the detailed properties of a place returned by the Geoapify API.
 */
data class GeoapifyProperties(
    val name: String?,
    val city: String?,
    val country: String?,
    @SerializedName("address_line2") val fullAddress: String?,
    val lat: Double?,
    val lon: Double?,
    @SerializedName("place_id") val placeId: String
)
