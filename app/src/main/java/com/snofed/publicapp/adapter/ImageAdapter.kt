package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.ItemImageWithTextBinding
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.workoutfeed.Daum
import com.snofed.publicapp.models.workoutfeed.WorkoutImage
import com.snofed.publicapp.models.workoutfeed.WorkoutPointResponse
import com.snofed.publicapp.utils.Constants

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

class ImageAdapter( private val itemCount: Int) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var imageList: List<WorkoutImage> = listOf()

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.feedImgGallery)
        val closeIcon: ImageView = itemView.findViewById(R.id.closeIcon)
       // val imageCount: TextView = itemView.findViewById(R.id.imageCount)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFeedImage(clubs: List<WorkoutImage>?) {
        if (clubs != null) {
            this.imageList = clubs
        }

        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_with_text, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Set the image
        val item = imageList[position]

        if (item.path.equals("")) {
            Glide.with(holder.imageView).load(R.drawable.feed_view1).into(holder.imageView)
        } else {
            Glide.with(holder.imageView).load(Constants.BASE_URL_IMAGE+item.path).diskCacheStrategy(
                DiskCacheStrategy.ALL).fitCenter().into(holder.imageView)
        }

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

