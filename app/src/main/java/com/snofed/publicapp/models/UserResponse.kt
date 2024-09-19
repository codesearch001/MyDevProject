package com.snofed.publicapp.models

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
    val gender: Long,
    val weight: Long,
    val age: Long,
    val isSubscribed: Boolean,
    val publicUserSettings: List<PublicUserSetting>,
    val favouriteClients: List<String>,
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val username: String,
    val phone: Any?,
    val cellphone: Any?,
    val isConfirmed: Boolean,
    val isDeleted: Boolean,
    val password: Any?,
    val roleName: Any?,
    val roleId: Any?,
    val clientName: Any?,
    val clientId: Any?,
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
