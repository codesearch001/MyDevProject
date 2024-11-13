package com.snofed.publicapp.ui.setting

import com.snofed.publicapp.models.workoutfeed.Daum

data class UploadResponse(
    val success: Boolean,
    val message: String,
    val data: String,
    val statusCode: Long,
    val totalItems: Long,
)