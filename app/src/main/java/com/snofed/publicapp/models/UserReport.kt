package com.snofed.publicapp.models

import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class UserReport(
    val id: String,
    val clientId: String,
    val categoryId: String,
    val categoryName: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val creatorId: String,
    val creatorFullName: String,
    val status: Int,
    val syncAction: Int,
    val notes: List<TaskNote> = emptyList(),

)
@Serializable
data class TaskNote(
    val id: String,
    val text: String,
    val creatorId: String,
    val creatorFullName: String,
    val taskId: String,
    val syncAction: Int,
    val taskMedia: List<TaskMedia>,
)
@Serializable
data class TaskMedia(
    val id: String,
    val path: String,
    val tempPath: String,
    val fileName: String,
    val mediaType: Int,
    val syncAction: Int,
    val taskNoteId: String,
)

/*
data class TaskHour(
    val id: String,
    val taskId: String,
    val hours: Int,
    val syncAction: Int,
    val lastUpdateDate: Date,
    val createdDate: Date,
    val isDeleted: Boolean
)*/
