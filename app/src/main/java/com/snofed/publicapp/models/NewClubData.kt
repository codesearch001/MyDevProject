package com.snofed.publicapp.models

//data class NewClubData()
data class NewClubData(
    val success: String,
    val message: Any,
    val statusCode: Int,
    val totalItems: Int,
    val outerArray: List<OuterItem>
)

data class OuterItem(
    val innerArray: List<ClientItem>
)
data class ClientItem(
//    val id: Int,
//    val name: String

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