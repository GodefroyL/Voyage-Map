package com.example.voyage_map.data.api

import com.google.gson.annotations.SerializedName

// --- Root Response ---
data class GeoapifyResponse(
    val features: List<GeoapifyFeature>
)

// --- Feature and Geometry ---
data class GeoapifyFeature(
    val properties: GeoapifyProperties,
    val geometry: Geometry
)

data class Geometry(
    val coordinates: List<Double>
)

// --- Properties and Sub-Models for Ranking ---
data class GeoapifyProperties(
    @SerializedName("place_id") val placeId: String,

    // Fields for UI display
    val name: String?,
    @SerializedName("address_line2") val fullAddress: String?,

    // Fields for ranking
    val categories: List<String>?,
    @SerializedName("wiki_and_media") val wikiAndMedia: WikiAndMedia?,

    // ADD THESE TWO PROPERTIES
    val rank: Rank?,
    @SerializedName("place_rank") val placeRank: Int?
)

// ADD THIS DATA CLASS
data class Rank(
    val importance: Double?
)

data class WikiAndMedia(
    val wikipedia: String?
)
