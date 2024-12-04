package com.snofed.publicapp.models.browseSubClub

import com.snofed.publicapp.models.realmModels.Club

/*
class BrowseSubClubResponse {
}*/
/*data class BrowseSubClubResponse(
    val success: Boolean,
    val message: Any?,
    val data: ClubData,
    val statusCode: Long,
    val totalItems: Long,
)*/

data class BrowseSubClubResponse(
    val success: Boolean,
    val message: Any?,
    val data: Club,
    val statusCode: Long,
    val totalItems: Long,
)

data class ClubData(
    val publicName: Any?,
    val country: Any?,
    val logoPath: Any?,
    val location: Any?,
    val startLongitude: String,
    val startLatitude: String,
    val description: Any?,
    val ticketing: Boolean,
    val personalization: Boolean,
    val hasManintenance: Boolean,
    val visibility: Long,
    val clientRating: Double,
    val totalRatings: Double,
    val totalTrails: Long,
    val totalTrailsLength: Long,
    val isSubscribed: Boolean,
    val areas: List<Area>,
    val activities: List<Activity>,
    val trails: List<Trail>,
    val intervals: List<Any?>,
    val banners: List<Any?>,
    val zones: List<Zone>,
    val events: List<Event>,
    val difficulties: List<Difficulty2>,
    val pois: List<Poi>,
    val resourceTypes: List<ResourceType>,
    val resources: List<Resource>,
    val license: License,
    val serviceAuthenticationToken: Any?,
    val publicData: PublicData,
    val parentOrganisation: ParentOrganisation,
    val subOrganisations: List<SubOrganisation>,
    val id: String,
    val syncAction: Long,
)

data class Area(
    val name: String,
    val description: String,
    val clientId: String,
    val twitter: Any?,
    val id: String,
    val syncAction: Long,
)

data class Activity(
    val name: String,
    val description: Any?,
    val iconPath: String,
    val id: String,
    val syncAction: Long,
)

data class Trail(
    val name: String,
    val shortName: Any?,
    val description: String?,
    val shortMessage: String,
    val length: Long,
    val hasLights: Boolean,
    val timespanLightsOn: String,
    val hasFee: Boolean,
    val fee: Any?,
    val comment: Any?,
    val isClosed: Boolean,
    val statusText: Any?,
    val statusDate: Any?,
    val connectFirstAndLastTrack: Boolean,
    val connectInternal: Boolean,
    val isPublished: Boolean,
    val maxAlt: Double,
    val minAlt: Double,
    val startAlt: Any?,
    val endAlt: Any?,
    val ownTrailMapColor: Any?,
    val iconText: Any?,
    val dirtyTime: Any?,
    val sortOrder: Long,
    val displayInTable: Boolean,
    val duration: Double,
    val lastPreparedDate: String,
    val preparationDate: String?,
    val modes: Any?,
    val trailIconPath: Any?,
    val lastUpdateDate: String,
    val visibility: Long,
    val areaId: String,
    val status: Int,
    val polyLine: PolyLine,
    val isActiveSkidSparService: Boolean,
    val skidSparServiceDays: Long,
    val skidSparLastPreparation: String,
    val activity: Activity2,
    val trailQuality: TrailQuality,
    val difficulty: Difficulty,
    val images: List<Imagee>,
    val intervalType: Long,
    val trailRatings: List<TrailRating>,
    val averageRating: Double,
    val startLatitude: Double,
    val startLongitude: Double,
    val excludeFromPreparation: Boolean,
    val isProTrail: Boolean,
    val id: String,
    val syncAction: Long,
)

data class PolyLine(
    val features: List<Feature>,
    val type: String,
)

data class Feature(
    val properties: Properties,
    val geometry: Geometry,
    val type: String,
)

data class Properties(
    val color: String,
    val activity: Any?,
    val status: Any?,
    val statusHistory: Any?,
    val trailId: String,
    val trailName: String,
)

data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String,
)

data class Activity2(
    val clientActivities: List<ClientActivity>,
    val name: String,
    val nameTranslates: NameTranslates,
    val description: Any?,
    val coverPath: Any?,
    val iconPath: String,
    val available: Boolean,
    val intervalType: Long,
    val id: String,
    val syncAction: Long,
)

data class ClientActivity(
    val clientId: String,
    val activityId: String,
)

data class NameTranslates(
    val en: String,
    val sv: String,
    val de: String,
    val no: Any?,
)

data class TrailQuality(
    val name: String,
    val nameTranslates: NameTranslates2,
    val order: Long,
    val id: String,
    val syncAction: Long,
)

data class NameTranslates2(
    val en: String,
    val sv: String,
    val de: Any?,
    val no: Any?,
)

data class Difficulty(
    val sortOrder: Long,
    val name: String,
    val color: String,
    val nameTranslates: NameTranslates3,
    val id: String,
    val syncAction: Long,
)

data class NameTranslates3(
    val en: String,
    val sv: String,
    val de: String,
    val no: Any?,
)

data class Imagee(
    val id: String,
    val path: String,
    val tempPath: Any?,
    val isDeleted: Boolean,
)

data class TrailRating(
    val trailId: String,
    val publicUserId: String,
    val rating: Double,
)

data class Zone(
    val name: String,
    val description: String,
    val visibility: Long,
    val status: Long,
    val color: String,
    val openDate: Any?,
    val closeDate: Any?,
    val definitions: List<Definition>,
    val images: List<Any?>,
    val zoneTypeId: String,
    val areaId: String,
    val clientId: String,
    val link: Any?,
    val linkName: Any?,
    val alwaysShowZoneOnMap: Boolean,
    val id: String,
    val syncAction: Long,
)

data class Definition(
    val longitude: Double,
    val latitude: Double,
)

data class Event(
    val name: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val caption: Any?,
    val description: String,
    val coverImagePath: Any?,
    val iconPath: String,
    val sponsors: String,
    val sponsorList: List<Any?>,
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

data class Difficulty2(
    val name: String,
    val color: String,
    val sortOrder: Long,
    val id: String,
    val syncAction: Long,
)

data class Poi(
    val name: String,
    val description: Any?,
    val preparationDate: String,
    val lastUpdateDate: String,
    val poiTypeId: String,
    val clientId: String,
    val areaId: String,
    val poiStatusHistories: List<PoiStatusHistory>,
    val images: List<Any?>,
    val lastPoiStatus: Any?,
    val poiVisibility: Long,
    val latitude: Double,
    val longitude: Double,
    val isNotification: Boolean,
    val isNotificationInactive: Boolean,
    val notificationActiveFrom: Any?,
    val notificationActiveTo: Any?,
    val link: Any?,
    val linkName: Any?,
    val id: String,
    val syncAction: Long,
)

data class PoiStatusHistory(
    val statusText: String?,
    val poiTypeStatusId: String,
    val poiTypeStatus: PoiTypeStatus,
    val statusDate: String,
    val userId: String,
    val user: Any?,
    val id: String,
    val syncAction: Long,
)

data class PoiTypeStatus(
    val name: String,
    val color: String,
    val poiTypeId: String,
    val poiTypeName: Any?,
    val clientId: String,
    val nameTranslates: NameTranslates4,
    val id: String,
    val syncAction: Long,
)

data class NameTranslates4(
    val en: String,
    val sv: String,
    val de: String,
    val no: Any?,
)

data class ResourceType(
    val name: String,
    val description: Any?,
    val hoursBackPresentation: Long,
    val isLocked: Boolean,
    val mapIconPath: Any?,
    val filterIconPath: Any?,
    val id: String,
    val syncAction: Long,
)

data class Resource(
    val resourceId: Any?,
    val friendlyName: String,
    val description: String,
    val allowNoRoute: Boolean,
    val isActive: Boolean,
    val isPublic: Boolean,
    val brand: String,
    val model: String,
    val isMobileUnit: Boolean,
    val resourceRegisterId: String,
    val isGpsTrackerId: Boolean,
    val isForTrailPreparation: Boolean,
    val resourceType: ResourceType2,
    val clientRef: String,
    val id: String,
    val syncAction: Long,
)

data class ResourceType2(
    val name: String,
    val nameTranslates: NameTranslates5,
    val description: Any?,
    val hoursBackPresentation: Long,
    val isLocked: Boolean,
    val iconPath: String,
    val id: String,
    val syncAction: Long,
)

data class NameTranslates5(
    val en: String,
    val sv: String,
    val de: String,
    val no: Any?,
)

data class License(
    val ticketing: Boolean,
    val personalization: Boolean,
    val proTrails: Boolean,
    val createdDate: String,
    val trialPeriod: Long,
    val trialPeriodTo: String,
    val id: String,
    val syncAction: Long,
)

data class PublicData(
    val description: String,
    val videoPath: Any?,
    val coverImagePath: String,
    val images: List<Image>,
    val links: List<Link>,
    val socialMediaLinks: SocialMediaLinks,
    val id: String,
    val syncAction: Long,
)

data class Image(
    val path: String,
    val publicDataId: String,
    val id: String,
    val syncAction: Long,
)

data class Link(
    val id: String,
    val linkName: String,
    val link: String,
)

data class SocialMediaLinks(
    val twitterLink: String,
    val instagramLink: String,
    val facebookLink: String,
)
data class ParentOrganisation(
    val id: String,
    val publicName: String,
    val representative: String,
    val email: String,
    val phoneNumber: String,
    val additionalPhoneNumber: String,
    val visibility: String,
    val totalTrails: Long,
    val totalTrailsLength: Long,
    val logoPath: String,
    val location: String,
    val country: String,
    val county: String,
    val municipality: String,
    val license: Any?,
    val coordinates: Any?,
    val clientSettings: Any?,
    val intervals: Any?,
    val parentOrganisationId: Any?,
    val parentOrganisation: Any?,
    val subOrganisations: List<SubOrganisation>,
)

data class SubOrganisation(
    val id: String,
    val publicName: String,
    val representative: String,
    val email: String,
    val phoneNumber: String,
    val additionalPhoneNumber: String,
    val visibility: String,
    val totalTrails: Long,
    val totalTrailsLength: Long,
    val logoPath: String,
    val location: String,
    val country: String,
    val county: String,
    val municipality: String,
    val license: License2,
    val coordinates: Any?,
    val clientSettings: List<ClientSetting>,
    val intervals: List<Any?>,
    val parentOrganisationId: String,
    val subOrganisations: List<Any?>,
)

data class License2(
    val id: String,
    val name: String,
    val price: Double,
    val licenseType: Long,
    val trialPeriod: Long,
    val trialPeriodTo: String,
    val isPublicPlan: Boolean,
    val isFree: Boolean,
    val createdDate: String,
    val navigator: Boolean,
    val maximumStaffRoleUsers: Long,
    val areas: Boolean,
    val maximumAreas: Long,
    val resources: Boolean,
    val resourceReport: Boolean,
    val resourceStatistics: Boolean,
    val maximumTrails: Long,
    val trailPreparation: Boolean,
    val trailStatistics: Boolean,
    val skidspar: Boolean,
    val importTrails: Boolean,
    val personalization: Boolean,
    val proTrails: Boolean,
    val maximumPoi: Long,
    val defaultPoi: Boolean,
    val customPoi: Boolean,
    val zones: Boolean,
    val weatherStations: Boolean,
    val membership: Boolean,
    val notifications: Boolean,
    val banners: Boolean,
    val ticketing: Boolean,
    val ticketingReports: Boolean,
    val tasks: Boolean,
    val events: Boolean,
    val subOrganisations: Boolean,
)

data class ClientSetting(
    val value: String?,
    val key: String,
)
