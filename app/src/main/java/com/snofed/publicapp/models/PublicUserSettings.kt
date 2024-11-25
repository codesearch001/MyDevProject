package com.snofed.publicapp.models

import com.snofed.publicapp.dto.PublicUserSettingsDTO
import com.snofed.publicapp.dto.UserDTO

data class PublicUserSettings(
    var key: String? = null,
    val value: String? = null
)

fun PublicUserSettingsDTO.toPublicUserSettings(): PublicUserSettings {
    return PublicUserSettings(
        key = this.key,
        value = this.value
    )
}