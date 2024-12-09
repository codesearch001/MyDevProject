package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class License : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var personalization = false
    var ticketing = false
    var proTrails = false
    var trialPeriod = 0
    var trialPeriodTo: String? = null
}