package com.example.voyage_map.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface WikipediaApiService {

    // 🔍 Search Wikipedia pages
    @GET("w/api.php")
    suspend fun searchPages(
        @Query("action") action: String = "query",
        @Query("list") list: String = "search",
        @Query("srsearch") query: String,
        @Query("format") format: String = "json"
    ): WikiSearchResponse

    // 📄 Page summary (REST API)
    @GET("page/summary/{title}")
    suspend fun getPageSummary(
        @Path("title") title: String
    ): WikiSummaryResponse
}
