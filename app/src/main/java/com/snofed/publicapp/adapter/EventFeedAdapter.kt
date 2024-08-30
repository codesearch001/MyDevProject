package com.snofed.publicapp.adapter

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
import com.snofed.publicapp.models.browseSubClub.Event
import com.snofed.publicapp.models.events.EventResponseList
import com.snofed.publicapp.utils.DateTimeConverter

/*class EventFeedAdapter()*/
class EventFeedAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<EventFeedAdapter.ClubViewHolder>() {

    private var feedArray: List<EventResponseList> = listOf()
    val dateTimeConverter = DateTimeConverter()

    interface OnItemClickListener {
        fun onItemClick(eventId: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(clubs: List<EventResponseList>?) {
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
        dateTimeConverter.convertDateTime(reslult.startDate)//convert data
        val getDate=dateTimeConverter.datePartOnly
        val getDateOfMonth=dateTimeConverter.dateOfMonthPartOnly
       /* val newDate = SpannableString(getDate)
        newDate.setSpan(RelativeSizeSpan(2f), 0, newDate.length, 0) // set size*/
        holder.textStartDate.text = getDate
        holder.textDateOfMonth.text = getDateOfMonth
        holder.textEventName.text = reslult.name

        if (reslult.location == ""){
            holder.textEventLocation.text = "N/A"
        }else{
            holder.textEventLocation.text = reslult.location
        }
        holder.textMaxAttendees.text = reslult.maxAttendees.toString()
        if (reslult.ticketPrice.equals(0.0)){
            holder.textTicketPrice.text = "Free Ticket "
        }else{
            holder.textTicketPrice.text = reslult.ticketPrice.toString()
        }
        holder.eventCardId.setOnClickListener {
            Log.e("click..", "clickClubItem")
            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }
    }

    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textEventName: TextView = itemView.findViewById(R.id.textEventName)
        val textEventLocation: TextView = itemView.findViewById(R.id.textEventLocation)
        val textMaxAttendees: TextView = itemView.findViewById(R.id.txtMaxAttendees)
        val textTicketPrice: TextView = itemView.findViewById(R.id.txtTicketPrice)
        val textStartDate: TextView = itemView.findViewById(R.id.txtStartEventDate)
        val textDateOfMonth: TextView = itemView.findViewById(R.id.textDateOfMonth)
        val eventCardId: CardView = itemView.findViewById(R.id.eventCardId)
    }


}