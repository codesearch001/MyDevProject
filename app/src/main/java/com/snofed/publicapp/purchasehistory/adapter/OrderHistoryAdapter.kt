package com.snofed.publicapp.purchasehistory.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ClientViewModelRealm
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.enums.PaymentOrderStatus


class OrderHistoryAdapter(private val listener: OnItemClickListener, private val vmClientRealm: ClientViewModelRealm) : RecyclerView.Adapter<OrderHistoryAdapter.ClubViewHolder>(){

    private var orderHistoryArray: List<Order> = listOf()
    val dateTimeConverter = DateTimeConverter()

    // Define the OnItemClickListener interface
    interface OnItemClickListener {
        fun onItemClick(order: Order)
    }
    /*init {
        // Calculate the total price when the adapter is initialized and set it to the provided TextView
         totalPrice = calculateTotalPriceOfAllDaum()

    }*/

    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<Order>?) {
        if (clubs != null) {
            this.orderHistoryArray = clubs
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.common_layout, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val reslult = orderHistoryArray[position]
        
        holder.txt_order_title.text = vmClientRealm.getClientNameById(reslult.clientRef)
        dateTimeConverter.convertDateTime(reslult.createdDate)//convert data
        holder.tv_created_date.text =dateTimeConverter.outputFormatterOnlyDate
        holder.txt_total_price.text = reslult.totalPrice.toString()
        // Calculate the total ticket price directly in the adapter
        holder.txt_number_of_tickets.text = reslult.tickets.count().toString()

        holder.id_valid_to.isVisible = false

        val ticketOrderStatus: Int = reslult.ticketOrderStatus.toInt()


        val statusText = PaymentOrderStatus.fromStatus(ticketOrderStatus)
        holder.tv_status_approved.text = statusText

        // Update the visibility of status indicators (ImageView) based on the PaymentOrderStatus
        when (ticketOrderStatus) {
            PaymentOrderStatus.Canceled.status -> {
                holder.status_cancelled.visibility = View.VISIBLE
                holder.status_approved.visibility = View.GONE
            }
            PaymentOrderStatus.Approved.status -> {
                holder.status_approved.visibility = View.VISIBLE
                holder.status_cancelled.visibility = View.GONE
            }
            // You can add cases for other statuses if needed
            else -> {
                holder.status_cancelled.visibility = View.GONE
                holder.status_approved.visibility = View.GONE
            }
        }
        // Set item click listener
        holder.itemView.setOnClickListener {
            listener.onItemClick(reslult) // Call the listener with the clicked item data
        }
    }

    override fun getItemCount(): Int = orderHistoryArray.size

    // Function to calculate the total price of all Daum objects in the list
    private fun calculateTotalPriceOfAllDaum(): Double {
        return orderHistoryArray.sumOf { it.totalPrice }
    }

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_order_title: TextView = itemView.findViewById(R.id.txt_order_title)
        val tv_created_date: TextView = itemView.findViewById(R.id.tv_created_date)
        val txt_total_price: TextView = itemView.findViewById(R.id.txt_total_price)
        val tv_status_approved: TextView = itemView.findViewById(R.id.tv_status_approved)
        val status_cancelled: ImageView = itemView.findViewById(R.id.status_cancelled)
        val status_approved: ImageView = itemView.findViewById(R.id.status_approved)
        val txt_number_of_tickets: TextView = itemView.findViewById(R.id.txt_number_of_tickets)
        val id_valid_to: LinearLayout = itemView.findViewById(R.id.id_valid_to)
        val txt_valid_to: TextView = itemView.findViewById(R.id.txt_valid_to)


    }
}