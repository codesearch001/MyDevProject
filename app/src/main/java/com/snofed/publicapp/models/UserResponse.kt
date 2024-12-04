package com.snofed.publicapp.models

import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.UserRealm
import io.realm.RealmList

/*data class UserResponse(
    val token: String,
    val user: User,
    val message: String,
    val success: String,
    val username: String,
    val data: Data,
    val email: String
)*/

data class UserResponse(
    val success: Boolean,
    val message: String?,
    val data: userData,
    val statusCode: Long,
    val totalItems: Long,
)

data class userData(
    val gender: Int,
    val weight: Int,
    val age: Int,
    val isSubscribed: Boolean,
    val publicUserSettings: List<PublicUserSetting>,
    val favouriteClients: List<String>,
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val username: String,
    val phone: String?,
    val cellphone: String?,
    val isConfirmed: Boolean,
    val isDeleted: Boolean,
    val password: String?,
    val roleName: String?,
    val roleId: String?,
    val clientName: String?,
    val clientId: String?,
    val token: String,
    val identityToken: Any?,
    val emailType: Long,
    val userGroupId: String,
    val userResources: Any?,
    val clientCoordinates: List<Any?>,
    val clientLogo: Any?,
    val language: Any?,
    val license: Any?,
    val serviceAuthenticationToken: Any?,
)

data class PublicUserSetting(
    val key: String,
    val value: String?,
)

fun userData.toRealm(): UserRealm {
    return UserRealm().apply {
        id = id ?: ""
        email = email ?: ""
        firstName = firstName ?: ""
        lastName = lastName ?: ""
        fullName = fullName ?: ""
        username = username ?: ""
        phone = phone ?: ""
        cellphone = cellphone ?: ""
        isConfirmed = isConfirmed ?: false
        isDeleted = isDeleted
        password = password
        roleName = roleName
        clientName = clientName
        clientId = clientId
        token = token
        userGroupId = userGroupId
        gender = gender
        weight = weight
        age = age
        isSubscribed = isSubscribed
        favouriteClients = RealmList<String>().apply {
            favouriteClients?.let { addAll(it) }
        }
        publicUserSettings = RealmList<PublicUserSettingsRealm>().apply {
            publicUserSettings?.forEach { setting ->
                add(
                    PublicUserSettingsRealm(
                        key = setting.key,
                        value = setting.value ?: null
                    )
                )
            }
        }
    }
}
