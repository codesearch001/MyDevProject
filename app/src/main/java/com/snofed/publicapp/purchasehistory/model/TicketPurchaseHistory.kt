package com.snofed.publicapp.purchasehistory.model

import com.snofed.publicapp.models.Order

data class TicketPurchaseHistory(
    val success: Boolean,
    val message: Any?,
    val data: List<Order>,
    val statusCode: Long,
    val totalItems: Long,
)

data class TicketOrderDetails(
    val success: Boolean,
    val message: Any?,
    val data: Order,
    val statusCode: Long,
    val totalItems: Long,
)
