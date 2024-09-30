package com.snofed.publicapp.ui.order.model

data class TicketModel(
    var ticketType: String,
    var startDate: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var mobile: String,
    var price: Double
)
