package com.snofed.publicapp.models

data class ZoneType(
    val iconPath: String,
    val id: String,
    val isLocked: Boolean,
    val name: String,
    val syncAction: Int
)