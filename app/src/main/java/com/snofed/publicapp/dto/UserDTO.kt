package com.snofed.publicapp.dto

open class UserDTO(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val username: String,
    val phone: String,
    val cellphone: String,
    val isConfirmed: Boolean,
    val isDeleted: Boolean? = null,
    val password: String? = null,
    val roleName: String? = null,
    val clientName: String? = null,
    val clientId: String? = null,
    val token: String? = null,
    val userGroupId: String? = null,
    val gender: Int? = null,
    val weight: Int? = null,
    val age: Int? = null,
    val isSubscribed: Boolean? = null,
    val publicUserSettings: List<PublicUserSettingsDTO>? = null,
    val favouriteClients: List<String>? = null
)
