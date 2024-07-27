package com.snofed.publicapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "Client")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val ids: Int? = null,
    val country: String,
    val county: String,
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
    val publicData: List<PublicData>,
)

data class PublicData(
    val description: String,
    val videoPath: String,
    val coverImagePath: String,
    val images: String,
    val syncAction: Int
)