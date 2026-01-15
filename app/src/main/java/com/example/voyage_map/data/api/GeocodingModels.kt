package com.example.voyage_map.data.api

// Models for the Geoapify Geocoding API

data class GeocodingResponse(
    val features: List<GeocodingFeature>
)

data class GeocodingFeature(
    val properties: GeocodingProperties
)

data class GeocodingProperties(
    val lat: Double,
    val lon: Double,
    val city: String?,
    val country: String?
)
