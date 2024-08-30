package com.snofed.publicapp.models.workoutfeed


data class WorkoutActivites(
    val success: Boolean,
    val message: Any?,
    val data: Data,
    val statusCode: Long,
    val totalItems: Long,
)

data class Data(
    val publicUserId: String,
    val publisherFullname: String,
    val duration: Long,
    val distance: Double,
    val isPublic: Boolean,
    val averagePace: Double,
    val startTime: String,
    val calories: Double,
    val activityId: String,
    val activity: Activity2,
    val description: String,
    val workoutPoints: List<WorkoutPointResponse>,
    val leaderboardTime: Long,
    val workoutImages: List<WorkoutImage>,
    val id: String,
    val syncAction: Long,
)

data class Activity2(
    val clientActivities: Any?,
    val name: String,
    val nameTranslates: NameTranslatesResponse,
    val description: Any?,
    val coverPath: Any?,
    val iconPath: String,
    val available: Boolean,
    val intervalType: Long,
    val id: String,
    val syncAction: Long,
)

data class NameTranslatesResponse(
    val en: String,
    val sv: String,
    val de: String,
    val no: Any?,
)

data class WorkoutPointResponse(
    val id: String,
    val workoutRef: String,
    val longitude: Double,
    val latitude: Double,
    val speed: Double,
    val timestamp: String,
)

data class WorkoutImage(
    val path: String,
    val fileName: String,
    val workoutId: String,
    val id: String,
    val syncAction: Long,
)

