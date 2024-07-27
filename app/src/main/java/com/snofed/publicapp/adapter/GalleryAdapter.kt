package com.snofed.publicapp.adapter

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.ClubActionAdapter.GridViewHolder
import com.snofed.publicapp.databinding.ActionItemsBinding
import com.snofed.publicapp.databinding.GalleryListItemsBinding
import com.snofed.publicapp.databinding.RectangularItemLayoutBinding
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.browseSubClub.Image

import com.snofed.publicapp.models.browseSubClub.PublicData
import com.snofed.publicapp.models.workoutfeed.Daum
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.DateTimeConverter

/*class GalleryAdapter {}*/
class GalleryAdapter():RecyclerView.Adapter<GalleryAdapter.GridViewHolder>() {

    private var galleryArray: List<Image> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setGallery(clubs: List<Image>?) {
        if (clubs != null) {
            this.galleryArray = clubs
        }
        Log.i("test","sizearr "+galleryArray.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_items, parent, false)
        return GridViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        //val reslult=holder.bind(outerArray[position])
        val reslult = galleryArray[position]

        Log.e("galleryImage" ,"galleryImage " + reslult.path)
        if (reslult.path == "") {
            Glide.with(holder.imageViewGrid).load(R.drawable.gallery1).into(holder.imageViewGrid)
        } else {
            Glide.with(holder.imageViewGrid).load(Constants.BASE_URL_IMAGE + reslult.path)
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).fitCenter()
                .into(holder.imageViewGrid)
        }
    }

    override fun getItemCount(): Int = galleryArray.size

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewGrid: ImageView = itemView.findViewById(R.id.imageViewGrid)
    }
}





