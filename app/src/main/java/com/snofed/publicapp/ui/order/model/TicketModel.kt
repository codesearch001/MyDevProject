package com.snofed.publicapp.ui.order.model

import com.snofed.publicapp.ui.order.ticketing.TicketTypeDTO

data class TicketModel(
    var createdDate: String,
    var ticketStartDate: String,
    var buyerEmail: String,
    var buyerFirstName: String,
    var buyerLastName: String,
    var buyerMobileNumber: String,
    var licensePlateNumber: String,
    var ticketType: TicketTypeDTO,
)
