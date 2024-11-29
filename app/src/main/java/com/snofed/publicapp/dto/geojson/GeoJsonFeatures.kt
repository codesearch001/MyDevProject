package com.snofed.publicapp.dto.geojson

import android.os.Parcelable
import io.realm.annotations.Ignore
import kotlinx.parcelize.Parcelize

@Parcelize
open class GeoJsonFeatures(

    @Ignore
    val id: String? = null, // Realm will ignore this property

    val type: String? = null,
    val properties: GeoJsonFeatureProperties? = null,
    val geometry: GeoJsonGeometry? = null

) : Parcelable
