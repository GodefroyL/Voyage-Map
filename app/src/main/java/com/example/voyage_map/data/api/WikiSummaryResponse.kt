package com.example.voyage_map.data.api

data class WikiSummaryResponse(
    val title: String,
    val description: String?,
    val extract: String?,
    val thumbnail: Thumbnail?
)

data class Thumbnail(
    val source: String,
    val width: Int,
    val height: Int
)
