package com.snofed.publicapp.ui.feedback.model

data class FeedBackTaskCategories(
    val success: Boolean,
    val message: Any?,
    val data: List<FeedBackTaskCategoriesResponse>,
    val statusCode: Long,
    val totalItems: Long,
)

data class FeedBackTaskCategoriesResponse(
    val id: String,
    val name: String,
    val syncAction: Long,
)
