package com.snofed.publicapp.models.realmModels

import com.snofed.publicapp.dto.UserDTO
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserRealm : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var email: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var fullName: String? = null
    var username: String = ""
    var phone: String = ""
    var cellphone: String = ""
    var isConfirmed: Boolean = false
    var isDeleted: Boolean? = null
    var password: String? = null
    var roleName: String? = null
    var clientName: String? = null
    var clientId: String? = null
    var token: String? = null
    var userGroupId: String? = null
    var gender: Int? = null
    var weight: Int? = null
    var age: Int? = null
    var isSubscribed: Boolean? = null
    var favouriteClients: RealmList<String>? = null
    var publicUserSettings: RealmList<PublicUserSettingsRealm>? = null
}


fun UserRealm.toDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        fullName = this.fullName!!,
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