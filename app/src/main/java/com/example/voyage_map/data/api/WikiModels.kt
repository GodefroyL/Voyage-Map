package com.example.voyage_map.data.api

import com.google.gson.annotations.SerializedName

// =======================
// Search API Models
// =======================

data class WikiSearchResponse(
    @SerializedName("pages")
    val pages: List<WikiPage>
)

data class WikiPage(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?
)

// =======================
// Summary API Models
// =======================

data class WikiSummaryResponse(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("extract")
    val extract: String?,

    @SerializedName("thumbnail")
    val thumbnail: WikiThumbnail?,

    @SerializedName("coordinates")
    val coordinates: WikiCoordinates?
)

data class WikiThumbnail(
    @SerializedName("source")
    val source: String
)

data class WikiCoordinates(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)
