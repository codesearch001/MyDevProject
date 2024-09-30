package com.snofed.publicapp.models.membership

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Benefit(
    var id: String,
    var syncAction: Int,
    var name: String,
    var description: String,
    var partnerName: String,
    var partnerLogoPath: String,
    var partnerWebLink: String,
    var isActive: Boolean = false,
    var activeFrom: String,
    var activeTo: String
): Parcelable
