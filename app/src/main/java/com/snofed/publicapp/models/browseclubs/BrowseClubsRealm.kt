/*
package com.snofed.publicapp.models.browseclubs

import io.realm.RealmObject


open class RootRealm(
    var success: Boolean = false,
    var message: String? = null,
    var data: DataRealm? = null,
    var statusCode: Long = 0,
    var totalItems: Long = 0
) : RealmObject()

open class DataRealm(
    var systemData: SystemDataRealm? = null,
    var user: String? = null,
    var clients: List<ClientRealm> = listOf(),
    var serviceAuthenticationToken: String? = null
) : RealmObject()

open class SystemDataRealm(
    var activities: List<ActivityRealm> = listOf(),
    var poiTypes: List<PoiTypeRealm> = listOf(),
    var zoneTypes: List<ZoneTypeRealm> = listOf(),
    var taskCategories: List<TaskCategoryRealm> = listOf(),
    var resourceTypes: List<ResourceTypeRealm> = listOf(),
    var intervals: List<IntervalRealm> = listOf(),
    var helpArticles: List<HelpArticleRealm> = listOf(),
    var banners: List<BannerRealm> = listOf(),
    var weatherStationIconUrl: String? = null,
    var poiClosedBorderIconUrl: String? = null,
    var etaPointIconUrl: String? = null,
    var updatedPointIconUrl: String? = null
) : RealmObject()

open class ActivityRealm(
    var name: String? = null,
    var description: String? = null,
    var iconPath: String? = null,
    var id: String? = null,
    var syncAction: Long = 0
) : RealmObject()

open class PoiTypeRealm(
    var name: String? = null,
    var iconPath: String? = null,
    var isDefault: Boolean = false,
    var id: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class ZoneTypeRealm(
    var name: String? = null,
    var iconPath: String? = null,
    var isLocked: Boolean = false,
    var id: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class TaskCategoryRealm(
    var id: String? = null,
    var name: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class ResourceTypeRealm(
    var name: String? = null,
    var description: String? = null,
    var hoursBackPresentation: Int = 0,
    var isLocked: Boolean = false,
    var mapIconPath: String? = null,
    var filterIconPath: String? = null,
    var id: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class IntervalRealm(
    var intervalPeriodFrom: Int = 0,
    var intervalPeriodTo: Int = 0,
    var name: String? = null,
    var nameTranslates: NameTranslatesRealm? = null,
    var color: String? = null,
    var intervalType: Int = 0,
    var activities: List<Activity2Realm> = listOf(),
    var clientId: String? = null,
    var id: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class NameTranslatesRealm(
    var en: String? = null,
    var sv: String? = null,
    var de: String? = null,
    var no: String? = null
) : RealmObject()

open class Activity2Realm(
    var clientActivities: String? = null,
    var name: String? = null,
    var nameTranslates: NameTranslates2Realm? = null,
    var description: String? = null,
    var coverPath: String? = null,
    var iconPath: String? = null,
    var available: Boolean = false,
    var intervalType: Long = 0,
    var id: String? = null,
    var syncAction: Long = 0
) : RealmObject()

open class NameTranslates2Realm(
    var en: String? = null,
    var sv: String? = null,
    var de: String? = null,
    var no: String? = null
) : RealmObject()

open class HelpArticleRealm(
    var name: String? = null,
    var content: String? = null,
    var id: String? = null,
    var syncAction: Long = 0
) : RealmObject()

open class BannerRealm(
    var name: String? = null,
    var appImagePath: String? = null,
    var link: String? = null,
    var isActive: Boolean = false,
    var activeFrom: String? = null,
    var activeTo: String? = null,
    var clientId: String? = null,
    var id: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class ClientRealm(
    var publicName: String? = null,
    var country: String? = null,
    var county: String? = null,
    var logoPath: String? = null,
    var location: String? = null,
    var startLongitude: String? = null,
    var startLatitude: String? = null,
    var description: String? = null,
    var trialPeriod: Int = 0,
    var trialPeriodTo: String? = null,
    var hasTicketing: Boolean = false,
    var visibility: Int = 0,
    var coverImagePath: String? = null,
    var clientRating: String = 0.0,
    var totalRatings: String = 0,
    var activities: List<Activity3Realm> = listOf(),
    var clientSettings: List<ClientSettingRealm> = listOf(),
    var id: String? = null,
    var syncAction: Int = 0
) : RealmObject()

open class Activity3Realm(
    var name: String? = null,
    var description: String? = null,
    var iconPath: String? = null,
    var id: String? = null,
    var syncAction: Long = 0
) : RealmObject()

open class ClientSettingRealm(
    var value: String? = null,
    var key: String? = null
) : RealmObject()*/
