package com.snofed.publicapp.models




/*data class User(
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
    val publicUserSettings: List<PublicUserSettings>? = null,
    val favouriteClients: List<String>? = null
)*/

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var email: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var fullName: String = ""
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

open class PublicUserSettingsRealm : RealmObject() {
    var key: String? = null
    var value: String? = null
}




