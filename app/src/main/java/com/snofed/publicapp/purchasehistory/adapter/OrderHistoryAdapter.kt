package com.snofed.publicapp.purchasehistory.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.purchasehistory.model.Daum
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.enums.PaymentOrderStatus


class OrderHistoryAdapter() : RecyclerView.Adapter<OrderHistoryAdapter.ClubViewHolder>(){

    private var orderHistoryArray: List<Daum> = listOf()
    val dateTimeConverter = DateTimeConverter()

    /*init {
        // Calculate the total price when the adapter is initialized and set it to the provided TextView
         totalPrice = calculateTotalPriceOfAllDaum()

    }*/

    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<Daum>?) {
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

        //holder.txt_order_title.text =
        dateTimeConverter.convertDateTime(reslult.createdDate)//convert data
        holder.tv_created_date.text =dateTimeConverter.outputFormatterOnlyDate
        holder.txt_total_price.text = reslult.totalPrice.toString()
        // Calculate the total ticket price directly in the adapter
        holder.txt_number_of_tickets.text = reslult.tickets.count().toString()

        holder.id_valid_to.isVisible = false

        val ticketOrderStatus: Int = reslult.ticketOrderStatus.toInt()

        // Set the text based on the ticketOrderStatus
        holder.tv_status_approved.text = PaymentOrderStatus.fromStatus(ticketOrderStatus)

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
        val txt_number_of_tickets: TextView = itemView.findViewById(R.id.txt_number_of_tickets)
        val id_valid_to: LinearLayout = itemView.findViewById(R.id.id_valid_to)
        val txt_valid_to: TextView = itemView.findViewById(R.id.txt_valid_to)



    }
}