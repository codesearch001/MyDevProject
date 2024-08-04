package com.snofed.publicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.ItemImageWithTextBinding

/*class ImageAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_with_text, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}*/

class ImageAdapter(private val imageList: List<Int>, private val itemCount: Int) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val closeIcon: ImageView = itemView.findViewById(R.id.closeIcon)
       // val imageCount: TextView = itemView.findViewById(R.id.imageCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_with_text, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Set the image
        holder.imageView.setImageResource(imageList[position])

        /*// Set the image count
        val currentItem = position + 1
        holder.imageCount.text = "$currentItem/$itemCount"*/

        // Show or hide the close icon based on the position
        if (position == itemCount - 1) {

            holder.closeIcon.visibility = View.VISIBLE
        } else {
            holder.closeIcon.visibility = View.GONE
        }
        // Set click listener for the close icon
        holder.closeIcon.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}

