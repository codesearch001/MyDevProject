package com.snofed.publicapp.adapter

import android.R.attr.data
import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.models.workoutfeed.Daum
import com.snofed.publicapp.utils.DateTimeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class WorkoutFeedAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<WorkoutFeedAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var feedArray: List<Daum> = listOf()
    val dateTimeConverter = DateTimeConverter()


    interface OnItemClickListener {
        fun onItemClick(clientId: String)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<Daum>?) {
        if (clubs != null) {
            this.feedArray = clubs
        }
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        //val reslult=holder.bind(outerArray[position])
        val reslult =feedArray[position]
        holder.feedName.text = reslult.publisherFullname
        holder.feedDistance.text = String.format("%.1f", reslult.distance).toDouble().toString()+" Km"
        holder.feedDuration.text = reslult.duration.toString()
        val formattedDateTime = dateTimeConverter.convertDateTime(reslult.startTime)
        holder.feedStartTime.text =  dateTimeConverter.datePart
        holder.feedTime.text =  dateTimeConverter.timePart
        //println(timePart)
        println(formattedDateTime) // Output: "Jul 24, 2024, 14:30 h"
        println("Parsed LocalDateTime: ${formattedDateTime}")


       /* holder.cardIdLayout.setOnClickListener {

            Log.e("click.." , "clcllcl")

            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }*/
        /*   if (reslult.coverImagePath.equals("")) {
               Glide.with(binding.categoryImage).load(noImagePath).into(binding.categoryImage)
           } else {
               Glide.with(binding.categoryImage).load(imagePath + "/" + reslult.coverImagePath).diskCacheStrategy(
                   DiskCacheStrategy.ALL).fitCenter()
                   .into(binding.categoryImage)
           }*/
    }

    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedName: TextView = itemView.findViewById(R.id.feedName)
        val feedDistance: TextView = itemView.findViewById(R.id.feedDistance)
        val feedDuration: TextView = itemView.findViewById(R.id.feedDuration)
        val feedStartTime: TextView = itemView.findViewById(R.id.feedStartTime)
        val feedTime: TextView = itemView.findViewById(R.id.feedTime)


    }


}
