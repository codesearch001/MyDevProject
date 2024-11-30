package com.snofed.publicapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.databinding.ZonesTypeListBinding


class ZonesTypeAdapter(private val zonesList: List<String>) : RecyclerView.Adapter<ZonesTypeAdapter.TrailCategoryViewHolder>() {

    inner class TrailCategoryViewHolder(private val binding: ZonesTypeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String) {
            // Assuming you have an ImageView or similar to load the icon
            // Load the icon from the path into an ImageView
            binding.txtTrailCategory.text=  name

            // Log for debugging purposes
            Log.e("iconPathPraveen", "iconPathPraveen: $name")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailCategoryViewHolder {
        val binding = ZonesTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailCategoryViewHolder, position: Int) {
        holder.bind(zonesList[position])
    }

    override fun getItemCount(): Int = zonesList.size
}