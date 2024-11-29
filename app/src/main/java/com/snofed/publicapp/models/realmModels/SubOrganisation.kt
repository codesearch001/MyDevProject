package com.snofed.publicapp.models.realmModels

import com.snofed.publicapp.models.realmModels.ClientSetting
import com.snofed.publicapp.models.realmModels.License
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SubOrganisation: RealmObject() {

    @PrimaryKey
    var id: String = "" // Primary Key, initialized with a default value

    var publicName: String = ""
    var representative: String = ""
    var email: String = ""
    var phoneNumber: String = ""
    var additionalPhoneNumber: String? = null // Nullable for optional fields
    var visibility: String = ""
    var totalTrails: Long = 0
    var totalTrailsLength: Long = 0
    var logoPath: String? = null // Nullable for optional fields
    var location: String = ""
    var country: String = ""
    var county: String = ""
    var municipality: String = ""

    var license: License? = null // Assuming `License` is a `RealmObject`
    var coordinates: String? = null // Assuming coordinates are stored as a JSON String
    var clientSettings: RealmList<ClientSetting>? = RealmList() // Assuming `ClientSetting` is a `RealmObject`
    var intervals: RealmList<Interval>? = RealmList() // Assuming `Interval` is a `RealmObject`
    var parentOrganisationId: String? = null // Nullable if not always set
    var subOrganisations: RealmList<SubOrganisation>? = RealmList() // Self-referencing relationship
}