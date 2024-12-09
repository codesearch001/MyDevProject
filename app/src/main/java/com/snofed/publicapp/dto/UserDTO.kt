package com.snofed.publicapp.dto

import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.UserRealm
import io.realm.RealmList

data class UserDTO(
    var id: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var fullName: String?,
    var username: String,
    var phone: String?,
    var cellphone: String?,
    var isConfirmed: Boolean,
    var isDeleted: Boolean?,
    var password: String?,
    var roleName: String?,
    var clientName: String?,
    var clientId: String?,
    var token: String?,
    var userGroupId: String?,
    var gender: Int?,
    var weight: Int?,
    var age: Int?,
    var isSubscribed: Boolean?,
    var favouriteClients: List<String>?,
    var publicUserSettings: List<PublicUserSettingsDTO>?
)

// Extension function for UserRealm to UserDTO
fun UserRealm.toDTO(): UserDTO {
    return UserDTO(
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
        favouriteClients = this.favouriteClients?.toList(),
        publicUserSettings = this.publicUserSettings?.map { it.toDTO() }
    )
}

// Extension function for UserDTO to UserRealm
fun UserDTO.toRealm(): UserRealm {
    return UserRealm().apply {
        id = this@toRealm.id
        email = this@toRealm.email
        firstName = this@toRealm.firstName
        lastName = this@toRealm.lastName
        fullName = this@toRealm.fullName
        username = this@toRealm.username
        phone = this@toRealm.phone
        cellphone = this@toRealm.cellphone
        isConfirmed = this@toRealm.isConfirmed
        isDeleted = this@toRealm.isDeleted
        password = this@toRealm.password
        roleName = this@toRealm.roleName
        clientName = this@toRealm.clientName
        clientId = this@toRealm.clientId
        token = this@toRealm.token
        userGroupId = this@toRealm.userGroupId
        gender = this@toRealm.gender
        weight = this@toRealm.weight
        age = this@toRealm.age
        isSubscribed = this@toRealm.isSubscribed
        favouriteClients = this@toRealm.favouriteClients?.let { RealmList<String>().apply { addAll(it) } }
        publicUserSettings = this@toRealm.publicUserSettings?.let {
            RealmList<PublicUserSettingsRealm>().apply { addAll(it.map { dto -> dto.toRealm() }) }
        }
    }
}

