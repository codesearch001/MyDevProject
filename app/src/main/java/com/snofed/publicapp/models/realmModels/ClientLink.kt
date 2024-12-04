package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ClientLink : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var linkName: String? = null
    var link: String? = null
}