package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject

open class TrailRating : RealmObject() {
    var trailId: String = ""
    var publicUserId: String = ""
    var rating: Double? =null
}