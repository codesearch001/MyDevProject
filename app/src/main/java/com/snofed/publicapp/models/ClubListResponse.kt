package com.snofed.publicapp.models

data class ClubListResponse(
    val success: String,
    val message: Any,
    val statusCode: Int,
    val totalItems: Int,
    val data: Data
)

