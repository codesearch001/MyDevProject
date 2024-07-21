package com.snofed.publicapp.models

data class Client(
    val country: String,
    val coverImagePath: String,
    val description: Any,
    val hasTicketing: Boolean,
    val id: String,
    val location: String,
    val logoPath: String,
    val publicName: String,
    val startLatitude: String,
    val startLongitude: String,
    val syncAction: Int,
    val trialPeriod: Int,
    val trialPeriodTo: String,
    val visibility: Int,
    val activities: List<Activity>,
    val clientSettings: List<ClientSetting>,
)