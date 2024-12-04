package com.snofed.publicapp.dto.geojson

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class GeoJsonFeatureProperties(
    val color: String? = null,
    val activity: String? = null,
    val status: String? = null,
    val statusHistory: String? = null,
    val trailId: String? = null
) : Parcelable