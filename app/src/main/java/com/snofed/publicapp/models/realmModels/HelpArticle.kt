package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class HelpArticle : RealmObject(){
    @PrimaryKey
    var id: String? = null
    var name: String? = null
    var content: String? = null
    var syncAction = 0
}