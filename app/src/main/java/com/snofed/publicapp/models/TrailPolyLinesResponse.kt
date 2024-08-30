package com.snofed.publicapp.models

/*data class TrailPolyLinesResponse()*/

data class TrailPolyLinesResponse(
    val success: Boolean,
    val message: Any?,
    val data: DataPolyResponse,
    val statusCode: Long,
    val totalItems: Long,
)

data class DataPolyResponse(
    val features: List<FeaturePolyResponse>,
    val type: String,
)

data class FeaturePolyResponse(
    val properties: PropertiesPolyResponse,
    val geometry: GeometryPolyResponse,
    val type: String,
)

data class PropertiesPolyResponse(
    val color: String,
    val activity: Any?,
    val status: Any?,
    val statusHistory: Any?,
    val trailId: String,
    val trailName: String,
)

data class GeometryPolyResponse(
    val coordinates: List<List<Double>>,
    val type: String,
)

