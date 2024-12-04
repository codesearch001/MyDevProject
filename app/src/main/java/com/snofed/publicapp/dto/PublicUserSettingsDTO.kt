package com.snofed.publicapp.dto

import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm

open class PublicUserSettingsDTO(
    var key: String,
    var value: String? = null
)

fun PublicUserSettingsDTO.toRealm(): PublicUserSettingsRealm {
    return PublicUserSettingsRealm().apply {
        key = this@toRealm.key
        value = this@toRealm.value
    }
}