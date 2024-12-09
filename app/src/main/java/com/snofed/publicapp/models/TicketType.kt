package com.snofed.publicapp.models

data class TicketType(
    val id: String,
    val name: String,
    val ticketCategory: Long,
    val period: Long,
    val originalPrice: Double,
    val discount: Double,
    val totalPrice: Double,
    val numberOfVerifications: Long,
    val isRegistrationNumberRequired: Boolean,
    val isActive: Boolean,
    val isHidden: Boolean,
    val isCustomSale: Boolean,
    val customSaleFrom: Any?,
    val customSaleTo: Any?,
    val clientRef: String,
    val eventRef: String,
)