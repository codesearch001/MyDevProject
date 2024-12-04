package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Event : RealmObject(){

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null
    var startDate: String? = null
    var endDate: String? = null
    var location: String? = null
    var caption: String? = null
    var description: String? = null
    var coverImagePath: String? = null
    var sponsors: String? = null
    var link: String? = null
    var clientId: String? = null
    var ticketPrice: Double? = null
    var isChargeable = false
    var maxAttendees: Int? = null
    var trailId = RealmList<String>()
    var iconPath: String? = null
}