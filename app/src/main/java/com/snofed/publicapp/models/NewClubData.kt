package com.snofed.publicapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

//data class NewClubData()

data class NewClubData(
    val success: Boolean,
    val message: Any?,
    val data: Data,
    val statusCode: Long,
    val totalItems: Long,
)

data class Data(
    val serviceAuthenticationToken: String,
    val systemData: SystemData,
    val user: Any,
    val clients: List<Client>
)

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey val id: String,
    val country: String,
    val county: String,
    val coverImagePath: String,
    val description: String?,
    val hasTicketing: Boolean,
    var isInWishlist: Boolean,
    val location: String,
    val logoPath: String,
    val publicName: String,
    val clientRating: String,
    val totalRatings: String,
    val startLatitude: String,
    val startLongitude: String,
    val syncAction: Int,
    val trialPeriod: Int,
    val trialPeriodTo: String,
    val visibility: Int,
   // val activities: List<Activity> = emptyList(),
   // val clientSettings: List<ClientSetting> = emptyList(),
    @Relation(parentColumn = "id", entityColumn = "clientId")
    val publicData: List<PublicData?>? = emptyList(),
)

data class PublicData(
    val description: String,
    val videoPath: String,
    val coverImagePath: String,
    val images: String,
    val syncAction: Int
)

data class ClientSetting(
    val key: String,
    val value: String
)

data class Activity(
    val description: Any,
    val iconPath: String,
    val id: String,
    val name: String,
    val syncAction: Int
)


