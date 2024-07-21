package com.snofed.publicapp.models

data class Interval(
    val activities: List<ActivityXX>,
    val clientId: Any,
    val color: String,
    val id: String,
    val intervalPeriodFrom: Int,
    val intervalPeriodTo: Int,
    val intervalType: Int,
    val name: String,
    val nameTranslates: NameTranslatesX,
    val syncAction: Int
)