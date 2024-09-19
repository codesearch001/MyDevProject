package com.snofed.publicapp.ui.feedback


data class FeedApiResponse(
    val success: Boolean,
    val message: Any?,
    val data: List<Any?>,
    val statusCode: Long,
    val totalItems: Long,
)

