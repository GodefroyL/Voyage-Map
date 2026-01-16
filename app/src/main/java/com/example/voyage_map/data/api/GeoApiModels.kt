data class GeoapifyResponse(
    val features: List<GeoapifyFeature>
)

data class GeoapifyFeature(
    val properties: GeoapifyProperties
)

data class GeoapifyProperties(
    val name: String?,
    val categories: List<String>?,
    val formatted: String?,
    val lat: Double,
    val lon: Double
)
