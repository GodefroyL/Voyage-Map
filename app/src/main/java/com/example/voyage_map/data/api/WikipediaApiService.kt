package com.example.voyage_map.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WikipediaApiService {

    // 🔍 SEARCH (MediaWiki API)
    @GET("w/api.php")
    suspend fun searchPages(
        @Query("action") action: String = "query",
        @Query("list") list: String = "search",
        @Query("format") format: String = "json",
        @Query("srsearch") query: String
    ): WikiSearchResponse

    // 📄 PAGE SUMMARY (REST API)
    @GET("api/rest_v1/page/summary/{title}")
    suspend fun getPageSummary(
        @Path("title") title: String
    ): WikiSummaryResponse
}
