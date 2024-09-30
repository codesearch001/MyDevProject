package com.snofed.publicapp.ui.order.ticketing

data class OrderDTO(
    var id: String? = null,
    var ordertype: Int = 0,
    var orderLanguage: String? = null,
    var publicBuyerRef: String? = null,
    var tickets: String? = null,
    var clientRef: String? = null,
    var clientCallBackUrl: String? = null
)
