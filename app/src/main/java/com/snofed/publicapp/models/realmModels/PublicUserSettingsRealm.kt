package com.snofed.publicapp.models.realmModels

import io.realm.RealmObject

open class PublicUserSettingsRealm(
    var key: String = "",
    var value: String? = null
) : RealmObject()