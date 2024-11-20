package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserRealm : RealmObject() {
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

