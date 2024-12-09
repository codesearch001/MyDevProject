package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject

open class TrailPart : RealmObject()  {
    var trailPartOrder: Long? = null
    var trackId: String = ""
    var isTrackReversed: Boolean? = null
    var track: Track? = null
}