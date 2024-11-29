package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ResourceType : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var iconPath: String? = null
    var name: String? = null
    var description: String? = null
    var hoursBackPresentation: Int? = null
    var isLocked: Boolean? = null
}