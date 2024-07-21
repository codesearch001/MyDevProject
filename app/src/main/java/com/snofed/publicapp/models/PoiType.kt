package com.snofed.publicapp.models

data class PoiType(
    val iconPath: String,
    val id: String,
    val isDefault: Boolean,
    val name: String,
    val syncAction: Int
)