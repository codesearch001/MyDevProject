package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Banner :  RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var webImagePath: String? = null
    var appImagePath: String? = null
    var link: String? = null
    var isActive = false
    var activeFrom: String? = null
    var activeTo: String? = null
    var clientId: String? = null
    var hours: Int? = null
    var minutes: Int? = null
}