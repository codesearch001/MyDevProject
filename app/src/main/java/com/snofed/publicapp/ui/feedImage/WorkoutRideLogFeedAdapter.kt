package com.snofed.publicapp.ui.feedImage

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.models.workoutfeed.Daum
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.Helper

class WorkoutRideLogFeedAdapter() : RecyclerView.Adapter<WorkoutRideLogFeedAdapter.ClubViewHolder>() {

    private var feedArray: List<Daum> = listOf()
    val dateTimeConverter = DateTimeConverter()

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
        val reslult = feedArray[position]
        holder.feedName.isVisible =false
       // holder.feedLogTime.isVisible =true
        holder.feedDistance.text = String.format("%.2f", Helper.m2Km(reslult.distance)).toDouble().toString() + " km"
        val formattedDateTimeHMS = dateTimeConverter.formatSecondsToHMS(reslult.duration)
        holder.feedDuration.text = formattedDateTimeHMS
        val formattedDateTime = dateTimeConverter.convertDateTime(reslult.startTime)
        holder.feedStartTime.text = dateTimeConverter.datePart
        holder.feedTime.isVisible = true
        holder.feedTime.text = dateTimeConverter.timePart
        if(reslult.activity.name == "" ){
            holder.activityType.text = "N/A"
        }else{
            holder.activityType.text = reslult.activity.name
        }

        //println(timePart)
        println(formattedDateTime) // Output: "Jul 24, 2024, 14:30 h"
        println("Parsed LocalDateTime: ${formattedDateTime}")

        if (reslult.activity.iconPath.equals("")) {
            Glide.with(holder.imgActivitesIcon).load(R.drawable.bicycle).into(holder.imgActivitesIcon)
        } else {
            Glide.with(holder.imgActivitesIcon).load(reslult.activity.iconPath).diskCacheStrategy(
                DiskCacheStrategy.ALL).fitCenter().into(holder.imgActivitesIcon)
        }
    }

    override fun getItemCount(): Int = feedArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedName: TextView = itemView.findViewById(R.id.feedName)
       // val feedLogTime: TextView = itemView.findViewById(R.id.feedLogTime)
        val feedDistance: TextView = itemView.findViewById(R.id.feedDistance)
        val feedDuration: TextView = itemView.findViewById(R.id.feedDuration)
        val feedStartTime: TextView = itemView.findViewById(R.id.feedStartTime)
        val feedTime: TextView = itemView.findViewById(R.id.feedTime)
        val activityType: TextView = itemView.findViewById(R.id.activityType)
        val imgActivitesIcon: ImageView = itemView.findViewById(R.id.imgActivitesIcon)
        val llFeed: LinearLayout = itemView.findViewById(R.id.llFeed)
    }
}