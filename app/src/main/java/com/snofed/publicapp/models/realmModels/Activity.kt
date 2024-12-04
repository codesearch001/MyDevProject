package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Activities : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction = 0
    var name: String? = null
    var description: String? = null
    var iconPath: String? = null
    var intervalType = 0

}