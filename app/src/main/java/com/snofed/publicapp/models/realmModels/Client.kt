package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Client : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var country: String = ""
    var county: String = ""
    var coverImagePath: String? = null
    var description: String? = null
    var hasTicketing: Boolean = false
    var isInWishlist: Boolean = false
    var location: String = ""
    var logoPath: String? = null
    var publicName: String = ""
    var clientRating: String = ""
    var totalRatings: String = ""
    var startLatitude: String = ""
    var startLongitude: String = ""
    var syncAction: Int = 0
    var trialPeriod: Int = 0
    var trialPeriodTo: String = ""
    var visibility: Int = 0
    var activities: RealmList<Activities> = RealmList()
    var clientSettings: RealmList<ClientSetting> = RealmList()
    var publicData: RealmList<PublicData> = RealmList()
}
