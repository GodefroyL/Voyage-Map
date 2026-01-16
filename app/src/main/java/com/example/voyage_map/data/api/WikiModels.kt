package com.example.voyage_map.data.api

data class WikiSearchResponse(
    val query: Query?
)

data class Query(
    val search: List<WikiPage>
)

data class WikiPage(
    val id: Int,
    val title: String,
    val description: String?,   // ✅ ADD THIS
    val thumbnail: Thumbnail?
)