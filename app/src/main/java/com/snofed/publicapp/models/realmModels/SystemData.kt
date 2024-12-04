package com.snofed.publicapp.models.realmModels

import io.realm.RealmList

open class SystemData {
    var activities: RealmList<Activities>? = null
    var poiTypes: RealmList<PoiType>? = null
    var zoneTypes: RealmList<ZoneType>? = null
    var taskCategories: RealmList<TaskCategories>? = null
    var helpArticles: RealmList<HelpArticle>? = null
    var intervals: RealmList<Interval>? = null
    var weatherStationIconUrl: String? = null
    var poiClosedBorderIconUrl: String? = null
    var etaPointIconUrl: String? = null
    var updatedPointIconUrl: String? = null
}