package com.snofed.publicapp.dto

open class UserDTO(
    val id: String,
    val email: String,
    var firstName: String,
    var lastName: String,
    var fullName: String,
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
    var gender: Int? = null,
    var weight: Int? = null,
    var age: Int? = null,
    var isSubscribed: Boolean? = null,
    val publicUserSettings: List<PublicUserSettingsDTO>? = null,
    val favouriteClients: List<String>? = null
)
