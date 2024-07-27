package com.snofed.publicapp.models.clubActivities

/*class ActivitiesResponse {}*/
data class ActivitiesResponse(
    val success: Boolean,
    val message: Any?,
    val data: Data,
    val statusCode: Long,
    val totalItems: Long,
)

data class Data(
    val clientActivities: Any?,
    val name: String,
    val nameTranslates: NameTranslates,
    val description: Any?,
    val coverPath: Any?,
    val iconPath: String,
    val available: Boolean,
    val intervalType: Long,
    val id: String,
    val syncAction: Long,
)

data class NameTranslates(
    val en: String,
    val sv: String,
    val de: String,
    val no: Any?,
)
