package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PoiTypeStatus : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var color: String? = null
    var poiTypeId: String? = null
    var clientId: String? = null
}