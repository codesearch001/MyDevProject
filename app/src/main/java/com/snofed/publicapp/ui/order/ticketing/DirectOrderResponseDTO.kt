package com.snofed.publicapp.ui.order.ticketing


data class OrderResponseDTO(
    var id: String? = null,
    var d2IPaymentUrl: String? = null,
    var clientRef: String? = null,
    var orderPdfPath: String? = null,
    var clientCallBackUrl: String? = null
)