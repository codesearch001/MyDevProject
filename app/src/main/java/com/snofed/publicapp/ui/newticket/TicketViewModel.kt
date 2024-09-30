package com.snofed.publicapp.ui.newticket

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.ui.order.model.TicketModel

class TicketViewModel : ViewModel() {

    private val _tickets = mutableListOf<TicketModel>()
    val tickets: List<TicketModel> get() = _tickets.toList() // Expose an immutable list

    fun addTicket(ticket: TicketModel) {
        _tickets.add(ticket)
    }

    fun getTicketIndex(ticket: TicketModel): Int {
        return _tickets.indexOf(ticket)
    }
    fun removeTicketByIndex(index: Int) {
        _tickets.removeAt(index)
    }

    fun updatePrice(): Double {
        return _tickets.sumOf { it.price }
    }

    fun removeAllTicket() {
        _tickets.clear()
    }

    fun  isTicketListEmpty() : Boolean{
        return _tickets.size == 0
    }
    // New method to get all tickets
    fun getAllTickets(): List<TicketModel> {
        return _tickets.toList() // Returns an immutable copy
    }

    fun removeTicket(ticket: TicketModel) {
        _tickets.remove(ticket)
    }
}
