package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TaskCategories : RealmObject() {
    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var name: String? = null

}