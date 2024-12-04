package com.snofed.publicapp.models.realmModels

import com.snofed.publicapp.dto.PublicUserSettingsDTO
import io.realm.RealmObject

open class PublicUserSettingsRealm(
    var key: String = "",
    var value: String? = null
) : RealmObject()

fun PublicUserSettingsRealm.toDTO(): PublicUserSettingsDTO {
    return PublicUserSettingsDTO(
        key = this.key,
        value = this.value
    )
}