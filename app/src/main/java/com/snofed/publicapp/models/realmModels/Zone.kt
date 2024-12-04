package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Zone : RealmObject(){

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var description: String? = null
    var visibility: Int? = null
    var status: Int? = null
    var color: String? = null
    var openDate: String? = null
    var closeDate: String? = null
    var definitions: RealmList<ZoneDefinition>? = null
    var images: RealmList<ZoneImage>? = null
    var zoneTypeId: String? = null
    var areaId: String? = null
    var clientId: String? = null
    var link: String? = null
    var linkName: String? = null
    var alwaysShowZoneOnMap: Boolean? = null
}