package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ZoneImage : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var zoneId: String? = null
    var path: String? = null
    var tempPath: String? = null
    var syncAction: Int? = null
}