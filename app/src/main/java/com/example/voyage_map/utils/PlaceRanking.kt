package com.example.voyage_map.utils

import com.example.voyage_map.data.api.GeoapifyFeature

/**
 * Calculates a ranking score for a Geoapify place based on several criteria.
 * Higher score is better.
 */
fun GeoapifyFeature.score(): Int {
    var score = 0
    val categories = properties.categories.orEmpty()

    // 1. Category Priority (Most important)
    when {
        categories.any { it.contains("tourism.attraction") } -> score += 60
        categories.any { it.contains("tourism.museum") } -> score += 55
        categories.any { it.contains("heritage") } -> score += 50
        categories.any { it.contains("tourism.sights") } -> score += 40
    }

    // 2. Wikipedia Presence (Indicates significance)
    if (!properties.wikiAndMedia?.wikipedia.isNullOrEmpty()) {
        score += 25
    }

    // 3. Geoapify Importance Score (0.0 to 1.0)
    properties.rank?.importance?.let {
        score += (it * 20).toInt()
    }

    // 4. Place Rank (Lower is better, so we invert it)
    properties.placeRank?.let {
        // Coerce ensures the value doesn't go below 0 if rank is > 100
        score += (100 - it).coerceAtLeast(0)
    }

    return score
}
