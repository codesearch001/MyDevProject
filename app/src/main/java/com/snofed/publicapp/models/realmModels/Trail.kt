package com.snofed.publicapp.models.realmModels

import com.snofed.publicapp.dto.geojson.ListGeoJsonFeatures
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class Trail : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var shortName: String? = null
    var length: Int? = null
    var hasLights = false
    var timespanLightsOn: String? = null
    var hasFee = false
    var fee: String? = null
    var comment: String? = null
    var isClosed = false
    var statusText: String? = null
    var statusDate: String? = null
    var connectFirstAndLastTrack = false
    var connectInternal = false
    var isPublished = false
    var maxAlt: Double? = null
    var minAlt: Double? = null
    var startAlt: Double? = null
    var endAlt: Double? = null
    var ownTrailMapColor: String? = null
    var iconText: String? = null
    var dirtyTime: String? = null
    var sortOrder: Int? = null
    var displayInTable = false
    var duration: Double? = null
    var lastPreparedDate: String? = null
    var modes: String? = null
    var trailIconPath: String? = null
    var lastUpdateDate: String? = null
    var visibility: Int? = null
    var areaId: String? = null
    var status: Int? = null

    @Ignore
    var polyLine: ListGeoJsonFeatures? = null
    var isActiveSkidSparService = false
    var skidSparServiceDays: Int? = null
    var skidSparLastPreparation: String? = null
    var activity: Activities? = null
    var trailQuality: TrailQuality? = null
    var difficulty: Difficulty? = null
    var images: RealmList<TrailImage>? = null
    var averageRating: Double? = null
    var startLatitude: Double? = null
    var startLongitude: Double? = null
    var intervalType: Int? = null
    var description: String? = null
    var shortMessage: String? = null
    var preparationDate: String? = null
    var excludeFromPreparation = false
    var isProTrail = false
}