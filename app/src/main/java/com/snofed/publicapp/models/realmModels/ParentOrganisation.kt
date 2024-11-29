package com.snofed.publicapp.models.realmModels

import com.snofed.publicapp.models.realmModels.SubOrganisation
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class ParentOrganisation : RealmObject() {

    @PrimaryKey
    var id: String = "" // Primary key for the organisation

    var publicName: String = ""
    var representative: String = ""
    var email: String = ""
    var phoneNumber: String = ""
    var additionalPhoneNumber: String? = null // Nullable phone number
    var visibility: String = ""
    var totalTrails: Long = 0
    var totalTrailsLength: Long = 0
    var logoPath: String = ""
    var location: String = ""
    var country: String = ""
    var county: String = ""
    var municipality: String = ""

    var license: String? = null // If license is a String, change type accordingly
    var coordinates: String? = null // Assuming it's a String, change type accordingly
    var clientSettings: String? = null // Same as above
    var intervals: String? = null // Assuming intervals are stored as String, change as needed
    var parentOrganisationId: String? = null // Assuming it's a String, change type accordingly
    var parentOrganisation: ParentOrganisation? =
        null // If parentOrganisation is another RealmObject

    // List of SubOrganisation objects, RealmList is used for relationships
    var subOrganisations: RealmList<SubOrganisation> = RealmList()
}