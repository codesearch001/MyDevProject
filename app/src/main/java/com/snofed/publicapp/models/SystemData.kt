package com.snofed.publicapp.models


data class SystemData(
    val activities: List<Activity>,
    val banners: List<Banner>,
    val etaPointIconUrl: String,
    val helpArticles: List<Any>,
    val intervals: List<Interval>,
    val poiClosedBorderIconUrl: String,
    val poiTypes: List<PoiType>,
    val resourceTypes: List<ResourceType>,
    val taskCategories: List<TaskCategory>,
    val updatedPointIconUrl: String,
    val weatherStationIconUrl: String,
    val zoneTypes: List<ZoneType>
)