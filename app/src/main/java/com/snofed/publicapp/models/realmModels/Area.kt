package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Area : RealmObject()  {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var description: String? = null
    var clientId: String? = null
    var twitter: String? = null
}