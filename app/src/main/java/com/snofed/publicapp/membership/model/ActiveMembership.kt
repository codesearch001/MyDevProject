package com.snofed.publicapp.membership.model

import android.os.Parcel
import android.os.Parcelable


data class ActiveMembership(
    val success: Boolean,
    val message: Any?,
    val data: List<Daum>,
    val statusCode: Long,
    val totalItems: Long,
)

data class Daum(
    val membershipName: String,
    val clientRef: String,
    val totalPrice: Double,
    val isActive: Boolean,
    val validFrom: String,
    val validTo: String,
    val benefits: List<BenefitResponse>,
    val id: String,
    val createdDate: String,
    val syncAction: Long,
)

data class BenefitResponse(
    val name: String,
    val description: String,
    val partnerName: String,
    val partnerLogoPath: String,
    val partnerWebLink: String,
    val isActive: Boolean,
    val activeFrom: String,
    val activeTo: String,
    val id: String,
    val createdDate: String,
    val syncAction: Long,
)

