package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.models.browseSubClub.Event

/*class EventFeedAdapter()*/

class EventFeedAdapter() : RecyclerView.Adapter<EventFeedAdapter.ClubViewHolder>() {

    private var feedArray: List<Event> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(clubs: List<Event>?) {
        if (clubs != null) {
            this.feedArray = clubs
        }
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item_list, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val reslult =feedArray[position]

        holder.textEventName.text = reslult.name
    }

    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textEventName: TextView = itemView.findViewById(R.id.textEventName)
    }


}