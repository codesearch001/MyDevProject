package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.NewClubData


class BrowseClubListAdapter : RecyclerView.Adapter<BrowseClubListAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var outerArray: List<Client> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setClubs(clubs: List<Client>?) {
        if (clubs != null) {
            this.outerArray = clubs
        }
        Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_club_list, parent, false)
        return ClubViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val reslult =outerArray[position]
       //val reslult=holder.bind(outerArray[position])
        holder.tvName.text = reslult.publicName


    }

    override fun getItemCount(): Int = outerArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.lable1)


    }
}
