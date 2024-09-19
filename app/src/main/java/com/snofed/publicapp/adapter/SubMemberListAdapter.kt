package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter.OnItemClickListener
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.browseSubClub.ParentOrganisation
import com.snofed.publicapp.models.browseSubClub.SubOrganisation
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.ServiceUtil

class SubMemberListAdapter(private val listener: OnItemClickListener):  RecyclerView.Adapter<SubMemberListAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var outerArray: List<SubOrganisation> = listOf()


    interface OnItemClickListener {
        fun onItemClick(clientId: String)
    }




    @SuppressLint("NotifyDataSetChanged")
    fun setSubMemClubs(clubs: List<SubOrganisation>) {
        this.outerArray = clubs
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_club_list, parent, false)
        return ClubViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
       // val reslult=holder.bind(outerArray[position])
        val reslult = outerArray[position]
        //holder.clientRating.text = reslult.clientRating.toString()
        //holder.totalRatings.text = "(" + reslult.totalRatings.toString() + ")"
        holder.tvName.text = reslult.publicName
        holder.tvLable.text = reslult.county
        Log.e("reslult.publicName ..", "reslult.publicName" + reslult.publicName)
        holder.cardIdLayout.setOnClickListener {
            Log.e("click..", "clickClubItem")
            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }
        if (reslult.logoPath == null) {
            Glide.with(holder.backgroundImage).load(R.drawable.resort_card_bg)
                .into(holder.backgroundImage)
            //Glide.with(holder.background_image).load(Constants.BASE_URL_IMAGE).into(holder.background_image)
        } else {
            Glide.with(holder.backgroundImage)
                .load(ServiceUtil.BASE_URL_IMAGE + reslult.logoPath).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).fitCenter()
                .into(holder.backgroundImage)
        }
    }

    //override fun getItemCount(): Int = outerArray.size
    override fun getItemCount(): Int = outerArray.size
    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clientRating: TextView = itemView.findViewById(R.id.txtIdClubRating)
        val totalRatings: TextView = itemView.findViewById(R.id.txtIdTotalRating)
        val tvName: TextView = itemView.findViewById(R.id.lable1)
        val tvLable: TextView = itemView.findViewById(R.id.lable2)
        val cardIdLayout: LinearLayout = itemView.findViewById(R.id.cardIdLayout)
        val backgroundImage: ImageView = itemView.findViewById(R.id.clubBackgroundIdImage)
        val imgIdWishlist: ImageView = itemView.findViewById(R.id.imgIdWishlist)
    }
}