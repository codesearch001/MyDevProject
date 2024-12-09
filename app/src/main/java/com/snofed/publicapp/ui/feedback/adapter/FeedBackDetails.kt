package com.snofed.publicapp.ui.feedback.adapter


data class FeedBackDetails(
    val success: Boolean,
    val message: String,
    val data: Data,
    val statusCode: Long,
    val totalItems: Long,
)

data class Data(
    val id: String,
    val name: Any?,
    val creatorId: String,
    val creatorFullName: String,
    val clientId: String,
    val number: Long,
    val numberFormatted: String,
    val description: String,
    val location: Any?,
    val longitude: Double,
    val latitude: Double,
    val priority: Long,
    val dueDate: Any?,
    val estimatedTime: Any?,
    val status: Int,
    val categoryId: String,
    val categoryName: String,
    val notes: List<Note>,
    val assignedUserId: Any?,
    val assignedGroup: String,
    val assignedFullName: Any?,
    val hours: List<Any?>,
    val totalHoursSpent: Double,
    val isNavigatorReport: Boolean,
    val syncAction: Long,
    val isDeleted: Boolean,
    val lastUpdateDate: String,
    val createdDate: String,
)

data class Note(
    val id: String,
    val text: String,
    val creatorId: String,
    val taskId: String,
    val lastUpdateDate: String,
    val syncAction: Long,
    val createdDate: String,
    val creatorFullName: String,
    val isDeleted: Boolean,
    val taskMedia: List<Any?>,
    val isNavigatorNote: Boolean,
    val clientId: String,
)
