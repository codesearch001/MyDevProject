package com.snofed.publicapp.models

class RideApiResponse (
    val success: Boolean,
    val message: Any?,
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
    val activity: Any?,
    val description: String,
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
