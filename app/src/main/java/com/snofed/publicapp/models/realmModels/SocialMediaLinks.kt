package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class SocialMediaLinks : RealmObject() {

    var twitterLink: String? = null
    var instagramLink: String? = null
    var facebookLink: String? = null
}