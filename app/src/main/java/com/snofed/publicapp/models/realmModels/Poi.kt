package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Poi : RealmObject(){

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var description: String? = null
    var preparationDate: String? = null
    var lastUpdateDate: String? = null
    var poiVisibility = 0
    var poiTypeId: String? = null
    var clientId: String? = null
    var areaId: String? = null
    var poiStatusHistories: RealmList<PoiStatusHistory>? = null
    var images: RealmList<PoiImage>? = null
    var lastPoiStatus: PoiTypeStatus? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var isNotification = false
    var isNotificationInactive = false
    var notificationActiveFrom: String? = null
    var notificationActiveTo: String? = null
    var link: String? = null
    var linkName: String? = null
}