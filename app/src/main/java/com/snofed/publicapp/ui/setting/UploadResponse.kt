package com.snofed.publicapp.ui.setting

import com.snofed.publicapp.models.workoutfeed.Daum

data class UploadResponse(
    val success: Boolean,
    val message: String,
    val data: String,
    val statusCode: Long,
    val totalItems: Long,
)

data class UploadWorkoutResponse(
    val success: Boolean,
    val message: String,
    val data: List<WorkoutDataImages>,
    val statusCode: Long,
    val totalItems: Long,
)

data class WorkoutDataImages(
    val path: String,
    val fileName: String,
    val workoutId: String,
    val id: String,
    val syncAction: Long,
)
