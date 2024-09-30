package com.snofed.publicapp.ui.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.TicketListLayoutBinding
import com.snofed.publicapp.ui.order.ticketing.TicketDTO


class TicketAdapter(private var ticketList: List<TicketDTO>, private val onDeleteClick: (TicketDTO) -> Unit ) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TicketListLayoutBinding.inflate(inflater, parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]
        holder.bind(ticket)

       holder.deleteButton.setOnClickListener {
           onDeleteClick(ticket)
       }
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    // Method to update the list of tickets
    @SuppressLint("NotifyDataSetChanged")
    fun updateTickets(newTickets: List<TicketDTO>) {
        ticketList = newTickets
        notifyDataSetChanged()
    }

    class TicketViewHolder(val binding: TicketListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val deleteButton: ImageView = itemView.findViewById(R.id.img_remove)
        fun bind(ticket: TicketDTO) {
            binding.tvTitle.text = ticket.ticketType!!.name
            binding.txtStartDate.text = ticket.ticketStartDate
            binding.txtFirstName.text = ticket.buyerFirstName
            binding.txtLastName.text = ticket.buyerLastName
            binding.txtEmail.text = ticket.buyerEmail
            binding.txtPrice.text = ticket.ticketType!!.totalPrice.toString()
        }
    }
}
