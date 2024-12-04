package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PoiStatusHistory : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var statusText: String? = null
    var poiTypeStatusId: String? = null
    var poiTypeStatus: PoiTypeStatus? = null
    var statusDate: String? = null
    var userId: String? = null
    var user: UserRealm? = null
}