package com.snofed.publicapp.dto.geojson

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class ListGeoJsonFeatures(
    val type: String? = null,
    val features: List<GeoJsonFeatures>? = null
) : Parcelable
