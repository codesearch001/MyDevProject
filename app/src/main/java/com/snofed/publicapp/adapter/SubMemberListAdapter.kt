package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.models.realmModels.SubOrganisation
import com.snofed.publicapp.utils.ServiceUtil

class SubMemberListAdapter(private val listener: OnItemClickListener):  RecyclerView.Adapter<SubMemberListAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var outerArray: List<SubOrganisation> = listOf()
    private var favouriteClients: List<String> = listOf()


    interface OnItemClickListener {
        fun onItemClick(clientId: String)
    }




    @SuppressLint("NotifyDataSetChanged")
    fun setSubMemClubs(clubs: List<SubOrganisation>, favClients: List<String>) {
        this.outerArray = clubs
        this.favouriteClients = favClients
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_club_list, parent, false)
        return ClubViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
       // val reslult=holder.bind(outerArray[position])
        val subClub = outerArray[position]
        //holder.clientRating.text = reslult.clientRating.toString()
        //holder.totalRatings.text = "(" + reslult.totalRatings.toString() + ")"
        holder.tvName.text = subClub.publicName.trimStart().trimEnd()
        holder.tvLable.text = subClub.county
        Log.e("reslult.publicName ..", "reslult.publicName" + subClub.publicName)

        if (subClub.id in favouriteClients) {
            holder.imgIdWishlist.setImageResource(R.drawable.hearth_filled)
        } else {
            holder.imgIdWishlist.setImageResource(R.drawable.hearth_empty)
        }

        holder.cardIdLayout.setOnClickListener {
            Log.e("click..", "clickClubItem")
            listener.onItemClick(subClub.id) // Assuming Client has an 'id' property
        }
        if (subClub.logoPath == null) {
            Glide.with(holder.backgroundImage).load(R.drawable.resort_card_bg)
                .into(holder.backgroundImage)
            //Glide.with(holder.background_image).load(Constants.BASE_URL_IMAGE).into(holder.background_image)
        } else {
            Glide.with(holder.backgroundImage)
                .load(ServiceUtil.BASE_URL_IMAGE + subClub.logoPath).diskCacheStrategy(
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
