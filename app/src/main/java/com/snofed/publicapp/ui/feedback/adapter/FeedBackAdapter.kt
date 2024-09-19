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
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.ui.feedback.model.FeedBackTaskCategoriesResponse


class FeedBackAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<FeedBackAdapter.ClubViewHolder>() {


    private var feedArray: List<FeedBackTaskCategoriesResponse> = listOf()
    private var selectedPosition: Int = RecyclerView.NO_POSITION



    interface OnItemClickListener {
        fun onItemClick(id: String, name: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<FeedBackTaskCategoriesResponse>?) {
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
        //val reslult=holder.bind(outerArray[position])
        val reslult = feedArray[position]
        holder.feedBackName.text = reslult.name




        holder.card1.setOnClickListener {
            Log.e("click..", "clickClubItem" + reslult.id)
            listener.onItemClick(reslult.id,reslult.name) // Assuming Client has an 'id' property
        }
    }

    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedBackName: TextView = itemView.findViewById(R.id.feedBack_in_title)
        val card1: CardView = itemView.findViewById(R.id.card1)

    }
}