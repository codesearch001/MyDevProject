package com.snofed.publicapp.models

data class Banner(
    val activeFrom: String,
    val activeTo: String,
    val appImagePath: String,
    val clientId: String,
    val id: String,
    val isActive: Boolean,
    val link: String,
    val name: String,
    val syncAction: Int
)