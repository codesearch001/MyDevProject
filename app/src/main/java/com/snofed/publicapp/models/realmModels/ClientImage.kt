package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ClientImage : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var path: String? = null
    var publicDataId: String? = null
}