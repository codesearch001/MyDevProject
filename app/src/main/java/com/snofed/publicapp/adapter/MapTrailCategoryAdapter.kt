package com.snofed.publicapp.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.databinding.TrailCategoryListBinding


class MapTrailCategoryAdapter(private val trailList: List<String>) : RecyclerView.Adapter<MapTrailCategoryAdapter.TrailCategoryViewHolder>() {

    inner class TrailCategoryViewHolder(private val binding: TrailCategoryListBinding) :
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
        val binding = TrailCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailCategoryViewHolder, position: Int) {
        holder.bind(trailList[position])
    }

    override fun getItemCount(): Int = trailList.size
}
