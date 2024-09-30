package com.snofed.publicapp.models

import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class UserReport(
    val id: String,
    val clientId: String,
    val categoryId: String,
    val name: String,
    val categoryName: String,
    val description: String,
    val creatorFullName: String,
    val status: Int,
    val syncAction: Int,
    val longitude: Double,
    val latitude: Double,
    val notes: List<TaskNote> = emptyList(),

)
@Serializable
data class TaskNote(
    val id: String,
    val text: String,
    val creatorId: String,
    val taskId: String,
    val lastUpdateDate: Date,
    val syncAction: Int,
    val createdDate: Date,
    val creatorFullName: String,
    val isDeleted: Boolean,
    val taskMedia: List<TaskMedia>,
    val isNavigatorNote: Boolean,
    val clientId: String
)
@Serializable
data class TaskMedia(
    val id: String,
    val taskNoteId: String,
    val path: String,
    val tempPath: String,
    val mediaType: Int,
    val fileName: String
)

data class TaskHour(
    val id: String,
    val taskId: String,
    val hours: Int,
    val syncAction: Int,
    val lastUpdateDate: Date,
    val createdDate: Date,
    val isDeleted: Boolean
)