package com.snofed.publicapp.dto

import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.UserRealm
import io.realm.RealmList

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
    var publicUserSettings: List<PublicUserSettingsDTO>? = null,
    var favouriteClients: List<String>? = null
)

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
        publicUserSettings = this@toRealm.publicUserSettings?.let { RealmList<PublicUserSettingsRealm>().apply { addAll(it.map { dto -> dto.toRealm() }) } }
    }
}

