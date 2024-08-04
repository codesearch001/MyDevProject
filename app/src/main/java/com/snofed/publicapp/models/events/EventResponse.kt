package com.snofed.publicapp.models.events

/*
data class EventResponse()
*/
data class EventResponse(
    val success: Boolean,
    val message: Any?,
    val data: List<EventResponseList>,
    val statusCode: Long,
    val totalItems: Long,
)

data class EventResponseList(
    val name: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val caption: Any?,
    val description: String,
    val coverImagePath: String?,
    val iconPath: String,
    val sponsors: String,
    val sponsorList: List<SponsorList>,
    val link: Any?,
    val linkName: Any?,
    val clientId: String,
    val ticketPrice: Double,
    val isChargeable: Boolean,
    val maxAttendees: Long,
    val trailId: List<String>,
    val id: String,
    val syncAction: Long,
)

data class SponsorList(
    val name: String,
    val logoImagePath: String,
    val eventId: String,
    val id: String,
    val syncAction: Long,
)

