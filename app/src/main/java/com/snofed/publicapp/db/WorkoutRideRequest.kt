package com.snofed.publicapp.db

import com.google.gson.annotations.SerializedName
import io.realm.annotations.PrimaryKey
import java.util.UUID

data class WorkoutResponse(
    @SerializedName("activityId") val activityId: String,
    @SerializedName("averagePace") val averagePace: Double,
    @SerializedName("calories") val calories: Int,
    @SerializedName("description") val description: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("duration") val duration: Int,
    @SerializedName("id") val id: String,
    @SerializedName("isNewlyCreated") val isNewlyCreated: Boolean,
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("leaderboardTime") val leaderboardTime: Int,
    @SerializedName("publicUserId") val publicUserId: String,
    @SerializedName("publisherFullname") val publisherFullname: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("syncAction") val syncAction: Int,
    @SerializedName("workoutImages") val workoutImages: List<WorkoutImage>, // Assuming this is a list of image URLs
    @SerializedName("workoutPoints") val workoutPoints: List<WorkoutPoint>
)

data class WorkoutPoint(
    @SerializedName("id") val id: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("speed") val speed: Double,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("workoutRef") val workoutRef: String
)

/*data class WorkoutImage(
    @SerializedName("url") val url: String, // URL of the image
    @SerializedName("description") val description: String? // Optional description or other metadata
)*/


open class WorkoutImage(
    @SerializedName("id") var id: String = UUID.randomUUID().toString(), // Unique ID for the image, generated by default
    @SerializedName("sync_action") var syncAction: Int = 0,                      // Sync action flag (e.g., for syncing with server)
    @SerializedName("path") var path: String = "",                        // Path where the image is stored locally
    @SerializedName("file_name") var fileName: String = "",                    // Name of the image file
    @SerializedName("workout_id") var workoutId: String = ""                    // ID of the workout this image belongs to
)
