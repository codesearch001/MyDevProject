package com.snofed.publicapp.ui.order.ticketing

data class TicketDTO(
    var createdDate: String? = null,
    var ticketStartDate: String? = null,
    var buyerEmail: String? = null,
    var buyerFirstName: String? = null,
    var buyerLastName: String? = null,
    var buyerMobileNumber: String? = null,
    var licensePlateNumber: String? = null,
    var ticketType: TicketTypeDTO? = null
)
