package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TrailImage : RealmObject() {

    @PrimaryKey
    var path: String? = null
    var tempPath: String? = null
    var isDeleted = false
}