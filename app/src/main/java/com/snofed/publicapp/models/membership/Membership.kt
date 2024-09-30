package com.snofed.publicapp.models.membership


data class Membership(
    var id: String,
    var membershipName: String,
    var membershipNumber: String,
    var validFrom: String,
    var validTo: String,
    var clientRef: String,
    var publicBuyerRef: String,
    var createdDate: String,
    var buyerFirstName: String,
    var buyerLastName: String,
    var buyerEmail: String,
    var isMembershipActive: Boolean = false,
    var totalPrice: Double,
    var syncAction: Int,
    var isDeleted: Boolean = false,
    var isActive: Boolean = false,
    var isFromHistory: Boolean = false,
    var benefits: List<Benefit>
)



