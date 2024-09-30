package com.snofed.publicapp.ui.newticket

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.ui.order.ticketing.TicketDTO


class TicketViewModel : ViewModel() {

    private val _tickets = mutableListOf<TicketDTO>()
    val tickets: List<TicketDTO> get() = _tickets.toList() // Expose an immutable list

    fun addTicket(ticket: TicketDTO) {
        _tickets.add(ticket)
    }

    fun getTicketIndex(ticket: TicketDTO): Int {
        return _tickets.indexOf(ticket)
    }
    fun removeTicketByIndex(index: Int) {
        _tickets.removeAt(index)
    }

    fun updatePrice(): Double {
        return _tickets.sumOf { it.ticketType?.totalPrice ?: 0.0 }
    }


    fun removeAllTicket() {
        _tickets.clear()
    }

    fun  isTicketListEmpty() : Boolean{
        return _tickets.size == 0
    }
    // New method to get all tickets
    fun getAllTickets(): List<TicketDTO> {
        return _tickets.toList() // Returns an immutable copy
    }

    fun removeTicket(ticket: TicketDTO) {
        _tickets.remove(ticket)
    }
}
