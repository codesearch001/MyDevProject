package com.snofed.publicapp.models


data class TrailGraphData(
    val success: Boolean,
    val message: String,
    val data: List<GraphResponse>,
    val statusCode: Long,
    val totalItems: Long,
)

data class GraphResponse(
    val height: Long,
    val length: Long,
)
