package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.snofed.publicapp.models.realmModels.Client
import com.snofed.publicapp.models.Data
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.ServiceUtil

class ClubFavAdapter(private val context: Context,
                     private val listener: OnItemClickListener,
                     private val clubViewModel: AuthViewModel
)
    : RecyclerView.Adapter<ClubFavAdapter.ClubViewHolder>() {
    //val listener: OnItemClickListener
    //private var clubs: List<NewClubData> = listOf()
    private val clubs = mutableListOf<Client>()
    private var outerArray:  List<BrowseSubClubResponse> =  listOf()
    private var filteredClubs: List<BrowseSubClubResponse> = listOf()
    private val wishlistItems: MutableSet<String> = mutableSetOf()

    interface OnItemClickListener {
        fun onItemClick(clientId: String)
        //fun onWishlistClick(clientId: String) // Callback for wishlist icon clicks
    }
//    init {
//        filteredClubs = outerArray
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setClubs(newClubs: List<Client>) {
        clubs.clear()
        clubs.addAll(newClubs)
        notifyDataSetChanged()
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun setClubs(clubs: List<BrowseSubClubResponse>) {
//        if (clubs != null) {
//            this.outerArray = clubs
//        }
//        //this.filteredClubs = this.outerArray
//        Log.e("test1","sizearr "+outerArray)
//        Log.e("test2","sizearr "+clubs)
//        notifyDataSetChanged()
//    }

//    //Apply filter
//    fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val filteredResults = FilterResults()
//                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
//                filteredResults.values = if (filterPattern.isEmpty()) {
//                    outerArray
//                } else {
//                    outerArray.filter {
//                        it.publicName.lowercase().contains(filterPattern)
//                    }
//                }
//
//                return filteredResults
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                filteredClubs = results?.values as List<Client>
//                notifyDataSetChanged()
//            }
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_club_list, parent, false)
        return ClubViewHolder(view)
    }



    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        //val reslult=holder.bind(outerArray[position])
        val club = clubs[position]
        holder.clientRating.text = club.clientRating
        holder.totalRatings.text = "(" + club.totalRatings.toString() + ")"
        holder.tvName.text = club.publicName.toString()
        holder.tvLable.text = club.country.toString()

        holder.cardIdLayout.setOnClickListener {
            Log.e("click..", "clickClubItem")
            listener.onItemClick(club.id) // Assuming Client has an 'id' property
        }
        // Set wishlist icon based on the wishlist state
        if (wishlistItems.contains(club.id)) {
            holder.imgIdWishlist.setImageResource(R.drawable.hearth_filled)
        } else {
            holder.imgIdWishlist.setImageResource(R.drawable.hearth_filled)
        }
        /* holder.imgIdWishlist.setOnClickListener {
             listener.onWishlistClick(reslult.id)
         }*/
        // Handle wishlist icon click
        holder.imgIdWishlist.setOnClickListener {
            val clientId = club.id
            if (wishlistItems.contains(clientId)) {
                wishlistItems.remove(clientId)
            } else {
                wishlistItems.add(clientId)
            }
            notifyItemChanged(position) // Notify that the item at this position has changed
            //listener.onWishlistClick(clientId) // Notify the listener
        }

        if (club.logoPath == null) {
            Glide.with(holder.backgroundImage).load(R.drawable.resort_card_bg)
                .into(holder.backgroundImage)
            //Glide.with(holder.background_image).load(Constants.BASE_URL_IMAGE).into(holder.background_image)
        } else {
            Glide.with(holder.backgroundImage)
                .load(ServiceUtil.BASE_URL_IMAGE + club.logoPath).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).fitCenter()
                .into(holder.backgroundImage)
        }
    }

    //override fun getItemCount(): Int = outerArray.size
    override fun getItemCount(): Int = clubs.size
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