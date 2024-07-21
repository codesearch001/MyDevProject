package com.snofed.publicapp.models

data class ResourceType(
    val description: Any,
    val filterIconPath: Any,
    val hoursBackPresentation: Int,
    val id: String,
    val isLocked: Boolean,
    val mapIconPath: Any,
    val name: String,
    val syncAction: Int
)