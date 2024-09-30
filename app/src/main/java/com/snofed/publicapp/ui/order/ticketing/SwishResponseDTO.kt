package com.snofed.publicapp.ui.order.ticketing


data class SwishResponseDTO(
    var paymentRequestToken: String? = null,
    var location: String? = null,
    var orderId: String? = null
)