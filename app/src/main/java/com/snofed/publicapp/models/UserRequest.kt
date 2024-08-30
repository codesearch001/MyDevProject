package com.snofed.publicapp.models

data class UserRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean
)

data class UserRegRequest(
    val firstName: String,
    val email: String,
    val password: String,
    val lastName: String
)

data class UserRecoverRequest(
    val email: String,
)