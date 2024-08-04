package com.snofed.publicapp.models.workoutfeed

/*data class FeedListResponse()*/
/*
data class FeedListResponse(
    val success: Boolean,
    val message: Any?,
    val data: List<Daum>,
    val statusCode: Long,
    val totalItems: Long,
)

data class Daum(
    val publicUserId: String,
    val publisherFullname: String,
    val duration: Double,
    val distance: Double,
    val isPublic: Boolean,
    val averagePace: Double,
    val startTime: String,
    val calories: Double,
    val activityId: String,
    val description: Any?,
    val workoutPoints: List<WorkoutPoint>,
    val leaderboardTime: Long,
    val workoutImages: List<Any?>,
    val id: String,
    val syncAction: Long,
)

data class WorkoutPoint(
    val id: String,
    val workoutRef: String,
    val longitude: Double,
    val latitude: Double,
    val speed: Double,
    val timestamp: String,
)
*/

data class FeedListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Daum>,
    val statusCode: Long,
    val totalItems: Long,
)

data class Daum(
    val publicUserId: String,
    val publisherFullname: String,
    val duration: Long,
    val distance: Double,
    val isPublic: Boolean,
    val averagePace: Double,
    val startTime: String,
    val calories: Double,
    val activityId: String,
    val activity: Activity,
    val description: Any?,
    val workoutPoints: List<WorkoutPoint>,
    val leaderboardTime: Long,
    val workoutImages: List<Any?>,
    val id: String,
    val syncAction: Long,
)

data class Activity(
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

data class WorkoutPoint(
    val id: String,
    val workoutRef: String,
    val longitude: Double,
    val latitude: Double,
    val speed: Double,
    val timestamp: String,
)
