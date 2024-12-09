package com.snofed.publicapp.dto

import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm

open class PublicUserSettingsDTO(
    var key: String,
    var value: String? = null
)

// Extension function to convert PublicUserSettingsRealm to PublicUserSettingsDTO
fun PublicUserSettingsRealm.toDTO(): PublicUserSettingsDTO {
    return PublicUserSettingsDTO(
        key = this.key,
        value = this.value
    )
}

// Extension function to convert PublicUserSettingsDTO to PublicUserSettingsRealm
fun PublicUserSettingsDTO.toRealm(): PublicUserSettingsRealm {
    return PublicUserSettingsRealm().apply {
        key = this@toRealm.key
        value = this@toRealm.value
    }
}