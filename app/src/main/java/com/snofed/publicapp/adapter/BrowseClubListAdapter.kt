package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.models.ClubListResponse
import com.snofed.publicapp.models.Data
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.OuterItem

class BrowseClubListAdapter : RecyclerView.Adapter<BrowseClubListAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var outerArray: List<OuterItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setClubs(clubs: List<OuterItem>) {
        this.outerArray = clubs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_club_list, parent, false)
        return ClubViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        holder.bind(outerArray[position])
    }

    override fun getItemCount(): Int = outerArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(club: OuterItem) {
//            name.text = club.name
//            description.text = club.description
//            image.setImageResource(club.imageResId)
        }
    }
}
