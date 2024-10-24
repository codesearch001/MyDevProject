package com.snofed.publicapp.ui.feedback.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.ui.feedback.adapter.FeedBackAdapter.OnItemClickListener
import com.snofed.publicapp.ui.feedback.model.FeedBackByCategoriesIdResponse
import com.snofed.publicapp.utils.DateTimeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class FeedBackCategoriesAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<FeedBackCategoriesAdapter.ClubViewHolder>() {

    private var feedArray: List<FeedBackByCategoriesIdResponse> = listOf()

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<FeedBackByCategoriesIdResponse>?) {
        if (clubs != null) {
            this.feedArray = clubs
        }
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_back_category_list_item, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val reslult = feedArray[position]
        //holder.feedBackName.text = reslult.name

        if (reslult.categoryName != null){

            holder.feedBackName.text = reslult.categoryName.toString()
        }else{
            holder.feedBackName.text = "N/A"
        }

        holder.feedBackCreatedDate.isVisible =true
        val formattedDateto = convertDateTime(reslult.createdDate)
        holder.feedBackCreatedDate.text = formattedDateto

        holder.card1.setOnClickListener {
            Log.e("click..", "clickClubItem" + reslult.id)
            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(input: String): String {
        // Parse the input date-time string to LocalDateTime
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val localDateTime = LocalDateTime.parse(input, inputFormatter)

        // Define the output formatter to get the required format
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")

        // Adjust the date-time to the local time zone (optional)
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())

        // Return the formatted date-time string
        return outputFormatter.format(zonedDateTime)
    }



    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedBackName: TextView = itemView.findViewById(R.id.feedBack_in_title)
        val feedBackCreatedDate: TextView = itemView.findViewById(R.id.feedBack_created_date)
        val card1: CardView = itemView.findViewById(R.id.card1)

    }
}