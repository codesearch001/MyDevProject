package com.snofed.publicapp.ui.trailsstatus

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.TileViewListBinding
import com.snofed.publicapp.models.browseSubClub.Trail
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.enums.PageType

class TrailListAdapter(private var trails: List<Trail>, private val onItemClick: (Trail) -> Unit, private val onMapClick: (Trail) -> Unit, private val pageType: PageType) : RecyclerView.Adapter<TrailListAdapter.TrailViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        val binding =
            TileViewListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val trail = trails[position]
        holder.bind(trail, onItemClick, onMapClick, pageType)
    }

    override fun getItemCount(): Int = trails.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTrails(newTrails: List<Trail>) {
        trails = newTrails
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    class TrailViewHolder(private val binding: TileViewListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(
            trail: Trail,
            onItemClick: (Trail) -> Unit,
            onMapClick: (Trail) -> Unit,
            pageType: PageType
        ) {
            binding.trailName.text = trail.name
            //binding.length.text = trail.length.toString() + " m"
            //binding.length.text = Helper.m2Km(trail.length.toDouble()).toString() + R.string.t_km
            binding.length.text  = Helper.m2Km(trail.length.toDouble()).toString() +  binding.root.context.getString(R.string.t_km)
            val dateTimeConverter = DateTimeConverter()//Date format
            dateTimeConverter.convertDateTime(trail.lastUpdateDate)//convert data
            val getDate = dateTimeConverter.dateandtimePart
            binding.lastUpdateDate.text = getDate

            if (trail.status == 1) {
                binding.idOpen.isVisible = true
                binding.idClose.isVisible = false
            } else {
                binding.idClose.isVisible = true
                binding.idOpen.isVisible = false
            }

            if (trail.trailIconPath == null) {
                Glide.with(binding.backgroundImage).load(R.drawable.resort_card_bg)
                    .into(binding.backgroundImage)
            } else {
                Glide.with(binding.backgroundImage).load(trail.trailIconPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter()
                    .into(binding.backgroundImage)
            }
            // Handle click events
            binding.trailRL.setOnClickListener {
                onItemClick(trail)
            }

            /* // Handle map click
            binding.trailsMaps.setOnClickListener {
                onMapClick(trail)
            }*/

            // Handle map click based on page type
            if (pageType == PageType.MAP) {
                binding.trailsMaps.setOnClickListener {
                    onMapClick(trail)
                }
            }
        }
    }
}