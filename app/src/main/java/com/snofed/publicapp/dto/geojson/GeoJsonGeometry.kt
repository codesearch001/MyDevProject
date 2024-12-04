package com.snofed.publicapp.dto.geojson

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class GeoJsonGeometry(
    val coordinates: List<List<Double>>? = null,
    val type: String? = null
) : Parcelable