package com.snofed.publicapp.models

import com.snofed.publicapp.models.workoutfeed.NameTranslatesResponse


/*data class TrailsDetilsResponse(
    val success: Boolean,
    val message: Any?,
    val data: DataResponse,
    val statusCode: Long,
    val totalItems: Long,
)

data class DataResponse(
    val name: String,
    val nameTranslates: NameTranslatesResponse,
    val shortName: Any?,
    val description: String,
    val descriptionTranslates: DescriptionTranslates,
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
    val trailKmlParts: Any?,
    val lastPreparedDate: String,
    val modes: Any?,
    val trailIconPath: String,
    val lastUpdateDate: String,
    val visibility: Long,
    val status: Int,
    val polyLine: Any?,
    val isActiveSkidSparService: Boolean,
    val skidSparDays: List<Any?>,
    val skidSparLastPreparation: String,
    val client: ClientResponse,
    val area: Any?,
    val activity: Any?,
    val trailQuality: String,
    val difficulty: Any?,
    val images: List<Image>,
    val trailRatings: Double,
    val trailParts: List<TrailPart>,
    val intervals: List<Any?>,
    val averageRating: Double,
    val preparationDelay: Double,
    val willBePreparedIn: String,
    val lastPreparedTrailColor: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val excludeFromPreparation: Boolean,
    val isProTrail: Boolean,
    val id: String,
    val syncAction: Long,
)

data class NameTranslatesResponse(
    val en: String,
    val sv: Any?,
    val de: Any?,
    val no: Any?,
)

data class DescriptionTranslates(
    val en: String,
    val sv: Any?,
    val de: Any?,
    val no: Any?,
)

data class ClientResponse(
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
    val license: License,
    val coordinates: Any?,
    val clientSettings: List<ClientSettingResponse>,
    val intervals: Any?,
    val parentOrganisationId: Any?,
    val parentOrganisation: Any?,
    val subOrganisations: Any?,
)

data class License(
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

data class ClientSettingResponse(
    val value: String?,
    val key: Any?,
)

data class Image(
    val id: String,
    val path: String,
    val tempPath: Any?,
    val isDeleted: Boolean,
)

data class TrailPart(
    val trailPartOrder: Long,
    val trackId: String,
    val isTrackReversed: Boolean,
 val track: Any?,
)  */
/////////////////////////////////////

data class TrailsDetilsResponse(
    val success: Boolean,
    val message: Any?,
    val data: DataResponse,
    val statusCode: Long,
    val totalItems: Long,
)

data class DataResponse(
    val name: String,
    val nameTranslates: NameTranslatesResponse,
    val shortName: Any?,
    val description: String,
    val descriptionTranslates: DescriptionTranslates,
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
    val trailKmlParts: Any?,
    val lastPreparedDate: String,
    val modes: Any?,
    val trailIconPath: String,
    val lastUpdateDate: String,
    val visibility: Long,
    val status: Long,
    val polyLine: PolyLine,
    val isActiveSkidSparService: Boolean,
    val skidSparDays: List<Any?>,
    val skidSparLastPreparation: String,
    val client: ClientResponse,
    val area: Area,
    val activity: ActivityResponse,
    val trailQuality: String,
    val difficulty: Difficulty,
    val images: List<Image>,
    val trailRatings: List<TrailRating>,
    val trailParts: List<TrailPart>,
    val intervals: List<Any?>,
    val averageRating: Double,
    val preparationDelay: Double,
    val willBePreparedIn: String,
    val lastPreparedTrailColor: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val excludeFromPreparation: Boolean,
    val isProTrail: Boolean,
    val id: String,
    val syncAction: Long,
)

data class NameTranslatesResponse(
    val en: String,
    val sv: Any?,
    val de: Any?,
    val no: Any?,
)

data class DescriptionTranslates(
    val en: String,
    val sv: Any?,
    val de: Any?,
    val no: Any?,
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

data class ClientResponse(
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
    val license: License,
    val coordinates: Any?,
    val clientSettings: List<ClientSettingResponse>,
    val intervals: Any?,
    val parentOrganisationId: Any?,
    val parentOrganisation: Any?,
    val subOrganisations: Any?,
)

data class License(
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

data class ClientSettingResponse(
    val value: String?,
    val key: Any?,
)

data class Area(
    val name: String,
    val description: String,
    val clientId: String,
    val twitter: Any?,
    val id: String,
    val syncAction: Long,
)

data class ActivityResponse(
    val clientActivities: Any?,
    val name: String,
    val nameTranslates: NameTranslates2,
    val description: Any?,
    val coverPath: Any?,
    val iconPath: String,
    val available: Boolean,
    val intervalType: Long,
    val id: String,
    val syncAction: Long,
)

data class NameTranslates2(
    val en: String,
    val sv: String,
    val de: String,
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

data class Image(
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

data class TrailPart(
    val trailPartOrder: Long,
    val trackId: String,
    val isTrackReversed: Boolean,
    val track: Track,
)

data class Track(
    val beingDefined: Boolean,
    val currCoordsIndex: Long,
    val finishIndex: Long,
    val name: String,
    val description: Any?,
    val shortName: Any?,
    val length: Double,
    val lengthCheckPoints: Long,
    val checkPointsUpdate: Boolean,
    val isLowerPreparedStandard: Boolean,
    val getsPrepared: Boolean,
    val isProcessing: Boolean,
    val dirtyTime: Any?,
    val areaId: String,
    val clientId: String,
    val trackDefinitions: List<Any?>,
    val source: Long,
    val id: String,
    val syncAction: Long,
)

