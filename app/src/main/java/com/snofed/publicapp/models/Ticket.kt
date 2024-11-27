package com.snofed.publicapp.models

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