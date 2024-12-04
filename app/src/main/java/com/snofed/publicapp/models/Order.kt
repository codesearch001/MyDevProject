package com.snofed.publicapp.models

data class Order(
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