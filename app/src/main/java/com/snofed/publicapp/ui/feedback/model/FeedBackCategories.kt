package com.snofed.publicapp.ui.feedback.model


/*

data class FeedBackCategories(
    val success: Boolean,
    val message: Any?,
    val data: List<FeedBackByCategoriesIdResponse>,
    val statusCode: Long,
    val totalItems: Long,
)

data class FeedBackByCategoriesIdResponse(
    val id: String,
    val name: String,
    val nameTranslates: NameTranslates,
    val taskType: Long,
    val clientId: String,
    val syncAction: Long,
    val lastUpdateDate: String,
)

data class NameTranslates(
    val en: String,
    val sv: String,
    val de: Any?,
    val no: Any?,
)


*/
data class FeedBackCategories(
    val success: Boolean,
    val message: String?,
    val data: List<FeedBackByCategoriesIdResponse>,
    val statusCode: Long,
    val totalItems: Long,
)

data class FeedBackByCategoriesIdResponse(
    val id: String,
    val name: String,
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
    val estimatedTime: Double?,
    val status: Long,
    val categoryId: String,
    val categoryName: Any?,
    val notes: List<Any?>,
    val assignedUserId: String?,
    val assignedGroup: String,
    val assignedFullName: String?,
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

