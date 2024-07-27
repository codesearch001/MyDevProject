package com.snofed.publicapp.models

import androidx.room.Entity

//data class NewClubData()

data class NewClubData(
    val success: Boolean,
    val message: Any?,
    val data: Data,
    val statusCode: Long,
    val totalItems: Long,
)




