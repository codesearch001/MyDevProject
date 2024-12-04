package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PublicData : RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var description: String? = null
    var videoPath: String? = null
    var socialMediaLinks: SocialMediaLinks? = null
    var images: RealmList<ClientImage>? = null
    var links: RealmList<ClientLink>? = null
}