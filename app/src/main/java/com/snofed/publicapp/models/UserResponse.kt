package com.snofed.publicapp.models

data class UserResponse(
    val token: String,
    val user: User,
    val message: String,
    val success: String,
    val username: String,
    val email: String
)