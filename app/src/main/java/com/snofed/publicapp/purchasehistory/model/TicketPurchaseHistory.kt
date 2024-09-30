package com.snofed.publicapp.purchasehistory.model

data class TicketPurchaseHistory(
    val success: Boolean,
    val message: Any?,
    val data: List<Daum>,
    val statusCode: Long,
    val totalItems: Long,
)

data class Daum(
    val id: String,
    val createdDate: String,
    val singleOrderNumberFormatted: String,
    val ticketOrderStatus: Long,
    val paymentType: Long,
    val orderType: Long,
    val paymentSystemType: Long,
    val additionalInformation: Any?,
    val clientRef: String,
    val publicBuyerRef: String,
    val totalPrice: Double,
    val tickets: List<Ticket>,
)

data class Ticket(
    val id: String,
    val createdDate: String,
    val ticketStartDate: String,
    val ticketEndDate: String,
    val issuedDateOriginalTicketPrice: Double,
    val issuedDateTotalTicketPrice: Double,
    val buyerEmail: String,
    val buyerFirstName: String,
    val buyerLastName: String,
    val buyerFullName: String,
    val buyerMobileNumber: String,
    val licensePlateNumber: Any?,
    val validity: String,
    val ticketType: TicketType,
)

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
