package com.snofed.publicapp.purchasehistory.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.models.Order
import com.snofed.publicapp.models.Ticket
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.enums.PaymentOrderStatus

class OrderDetailsAdapter() : RecyclerView.Adapter<OrderDetailsAdapter.ClubViewHolder>(){

    private var orderHistoryDetailsArray: List<Ticket> = listOf()
    val dateTimeConverter = DateTimeConverter()
    var clientName: String? = ""


    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<Ticket>?, clientNameRef : String?) {
        if (clubs != null) {
            this.orderHistoryDetailsArray = clubs
        }
        clientName = clientNameRef
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history_details_list, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val ticket = orderHistoryDetailsArray[position]

            dateTimeConverter.convertDateTime(ticket.createdDate) // Convert date
            holder.tv_title.text = ticket.ticketType.name
            holder.txtStartDate.text = dateTimeConverter.outputFormatterOnlyDate
            holder.txtFirstName.text = ticket.buyerFirstName
            holder.txtLastName.text = ticket.buyerLastName
            holder.txtEmail.text = ticket.buyerEmail
            holder.txtPrice.text = ticket.issuedDateTotalTicketPrice.toString()

    }

    override fun getItemCount(): Int = orderHistoryDetailsArray.size



    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val txtStartDate: TextView = itemView.findViewById(R.id.txtStartDate)
        val txtFirstName: TextView = itemView.findViewById(R.id.txtFirstName)
        val txtLastName: TextView = itemView.findViewById(R.id.txtLastName)
        val txtEmail: TextView = itemView.findViewById(R.id.txtEmail)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
    }
}