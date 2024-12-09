package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Track : RealmObject(){

    @PrimaryKey
    var id: String = ""
    var syncAction: Int? = null
    var beingDefined = false
    var currCoordsIndex: Int? = null
    var finishIndex: Int? = null
    var name: String? = null
    var description: String? = null
    var shortName: String? = null
    var length: Double? = null
    var lengthCheckPoints: Int? = null
    var checkPointsUpdate = false
    var isLowerPreparedStandard = false
    var getsPrepared = false
    var dirtyTime: String? = null
    var area: Area? = null
}