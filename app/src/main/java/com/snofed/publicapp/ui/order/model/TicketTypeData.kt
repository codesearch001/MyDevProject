package com.snofed.publicapp.ui.order.model

data class TicketTypeData(
    val success: Boolean,
    val message: Any?,
    val data: List<Daum>,
    val statusCode: Long,
    val totalItems: Long,
)

data class Daum(
    val name: String,
    val nameTranslates: NameTranslates,
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
    val isFromEvent: Boolean,
    val services: Any?,
    val id: String,
    val createdDate: String,
    val syncAction: Long,
)

data class NameTranslates(
    val en: String,
    val sv: String,
    val de: Any?,
    val no: Any?,
)
