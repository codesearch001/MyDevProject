package com.snofed.publicapp.models.realmModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Club :  RealmObject() {

    @PrimaryKey
    var id: String? = null
    var syncAction: Int? = null
    var publicName: String? = null
    var country: String? = null
    var logoPath: String? = null
    var location: String? = null
    var startLongitude: Double? = null
    var startLatitude: Double? = null
    var description: String? = null
    var visibility: Int? = null
    var clientRating: Double? = null
    var totalRatings: Double? = null
    var totalTrails: Int? = null
    var totalTrailsLength: Int? = null
    var isSubscribed = false
    var areas: RealmList<Area>? = null
    var activities: RealmList<Activity>? = null
    var trails: RealmList<Trail>? = null
    var intervals: RealmList<Interval>? = null
    var banners: RealmList<Banner>? = null
    var zones: RealmList<Zone>? = null
    var events: RealmList<Event>? = null
    var difficulties: RealmList<Difficulty>? = null
    var pois: RealmList<Poi>? = null
    var resources: RealmList<Resource>? = null
    var license: License? = null
    var publicData: PublicData? = null
    var parentOrganisation: ParentOrganisation? = null
    var subOrganisations: RealmList<SubOrganisation>? = null
}