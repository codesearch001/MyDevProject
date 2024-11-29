package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Resource : RealmObject(){

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var friendlyName: String? = null
    var description: String? = null
    var allowNoRoute: Boolean? = null
    var isActive: Boolean? = null
    var isPublic: Boolean? = null
    var clientRef: String? = null
    var resourceType: ResourceType? = null
}