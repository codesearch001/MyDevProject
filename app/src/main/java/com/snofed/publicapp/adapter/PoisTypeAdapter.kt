package com.snofed.publicapp.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.PoisTypeListBinding



class PoisTypeAdapter(private val poiList: List<String>) : RecyclerView.Adapter<PoisTypeAdapter.PoisTypeViewHolder>() {

    inner class PoisTypeViewHolder(private val binding: PoisTypeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(iconPath: String) {
            // Assuming you have an ImageView or similar to load the icon
            // Load the icon from the path into an ImageView
            binding.imgPoisType.setImageURI(Uri.parse(iconPath))
            // Use Glide to load the image from the provided path
            Glide.with(binding.imgPoisType.context)
                .load(iconPath) // Load the image URL/path
                .placeholder(R.drawable.dinner) // Optional: Placeholder while loading
                .error(R.drawable.dinner) // Optional: Error image if load fails
                .into(binding.imgPoisType) // Set the image into the ImageView

            // Log for debugging purposes
            Log.e("iconPathPraveen", "iconPathPraveen: $iconPath")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoisTypeViewHolder {
        val binding = PoisTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoisTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoisTypeViewHolder, position: Int) {
        holder.bind(poiList[position])
    }

    override fun getItemCount(): Int = poiList.size
}
