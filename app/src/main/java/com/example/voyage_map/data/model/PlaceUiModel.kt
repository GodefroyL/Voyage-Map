package com.example.voyage_map.data.model

data class PlaceUiModel(
    val id: String,
    val name: String,
    val address: String?,
    val categories: List<String>?,
    val wikipediaUrl: String?,
    val isLiked: Boolean = false,
    val score: Int // Add this line
)
    