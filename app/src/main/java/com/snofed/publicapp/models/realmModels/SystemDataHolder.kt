package com.snofed.publicapp.models.realmModels

import io.realm.RealmList


open class SystemDataHolder {
        var systemData: SystemData? = null
        var userData: UserRealm? = null
        var clients: RealmList<Client>? = null
        var serviceAuthenticationToken: String? = null

}