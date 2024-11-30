package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Interval : RealmObject(){

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var intervalPeriodFrom: Int? = null
    var intervalPeriodTo: Int? = null
    var name: String? = null
    var color: String? = null
    var order: Int? = null
    var poiBorderPath: String? = null
    var clientId: String? = null
    var activities: RealmList<Activities>? = null
}