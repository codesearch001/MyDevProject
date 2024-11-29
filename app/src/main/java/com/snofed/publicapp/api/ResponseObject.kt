package com.snofed.publicapp.api

data class ResponseObject<T>(
    val success: Boolean,
    val message: String?,
    val data: T,
    val statusCode: Int,
    val totalItems: Int? = null
)