package com.snofed.publicapp.membership.model

import com.snofed.publicapp.models.membership.Membership

data class MembershipDetails(
    val success: Boolean,
    val message: Any?,
    val data: Membership,
    val statusCode: Long,
    val totalItems: Long,
)

/*
@Parcelize
data class Data(
    val membershipName: String,
    val clientRef: String,
    val totalPrice: Double,
    val isActive: Boolean,
    val validFrom: String,
    val validTo: String,
    val benefits: List<BenefitDetailsResponse>,
    val id: String,
    val createdDate: String,
    val syncAction: Long,
): Parcelable

@Parcelize
data class BenefitDetailsResponse(
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
): Parcelable
*/
