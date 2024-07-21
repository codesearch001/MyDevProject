package com.snofed.publicapp.models

data class Activity(
    val description: Any,
    val iconPath: String,
    val id: String,
    val name: String,
    val syncAction: Int
)