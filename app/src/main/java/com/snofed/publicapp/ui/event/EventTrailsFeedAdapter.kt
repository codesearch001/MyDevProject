package com.snofed.publicapp.ui.event

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.EventClubFeedAdapter
import com.snofed.publicapp.models.realmModels.Event
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.ServiceUtil

class EventTrailsFeedAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<EventTrailsFeedAdapter.ClubViewHolder>() {

    private var feedArray: List<Event> = listOf()
    val dateTimeConverter = DateTimeConverter()

    interface OnItemClickListener {
        fun onItemClick(eventId: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(clubs: List<Event>?) {
        if (clubs != null) {
            this.feedArray = clubs
        }
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.trails_event_list, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val event = feedArray[position]

        holder.textEventTrailsName.text = event.name

        val formattedDateTime = dateTimeConverter.convertDateTime(event.startDate!!)

        holder.textEventDate.text = dateTimeConverter.datePart +"," + dateTimeConverter.timePart

        println("Parsed LocalDateTime: ${event.coverImagePath}")

        if (event.coverImagePath.isNullOrEmpty()) {
            Glide.with(holder.imgActivitesIcon).load(R.drawable.resort_card_bg)
                .into(holder.imgActivitesIcon)
        } else {

            Glide.with(holder.imgActivitesIcon)
                .load(ServiceUtil.BASE_URL_IMAGE + event.coverImagePath).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).fitCenter().into(holder.imgActivitesIcon)
        }

        holder.eventCardId.setOnClickListener {
            Log.e("click..", "clickClubItem")
            listener.onItemClick(event.id!!) // Assuming Client has an 'id' property
        }
    }

    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textEventTrailsName: TextView = itemView.findViewById(R.id.textEventTrailsName)
        val textEventDate: TextView = itemView.findViewById(R.id.textEventDate)
        val imgActivitesIcon: ImageView = itemView.findViewById(R.id.imgActivitesIcon)
        val eventCardId: CardView = itemView.findViewById(R.id.eventCardId)
    }
}

