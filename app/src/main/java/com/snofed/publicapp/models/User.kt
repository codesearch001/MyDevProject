package com.snofed.publicapp.models

import com.snofed.publicapp.dto.UserDTO

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val fullName: String?,
    val username: String,
    val phone: String?,
    val cellphone: String?,
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
    val publicUserSettings: List<PublicUserSettings>? = null,
    val favouriteClients: List<String>? = null
)

fun UserDTO.toUser(): User {
    return User(
        id = this.id,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        fullName = this.fullName,
        username = this.username,
        phone = this.phone,
        cellphone = this.cellphone,
        isConfirmed = this.isConfirmed,
        isDeleted = this.isDeleted,
        password = this.password,
        roleName = this.roleName,
        clientName = this.clientName,
        clientId = this.clientId,
        token = this.token,
        userGroupId = this.userGroupId,
        gender = this.gender,
        weight = this.weight,
        age = this.age,
        isSubscribed = this.isSubscribed,
        publicUserSettings = this.publicUserSettings?.map { it.toPublicUserSettings() },
        favouriteClients = this.favouriteClients
    )
}






