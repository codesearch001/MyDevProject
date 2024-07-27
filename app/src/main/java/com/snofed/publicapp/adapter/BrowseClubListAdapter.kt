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
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.utils.Constants


class BrowseClubListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<BrowseClubListAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var outerArray: List<Client> = listOf()
    private var filteredClubs: List<Client> = listOf()

    interface OnItemClickListener {
        fun onItemClick(clientId: String)
    }

    init {
        filteredClubs = outerArray
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setClubs(clubs: List<Client>?) {
        if (clubs != null) {
            this.outerArray = clubs
        }
        this.filteredClubs = this.outerArray
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }

    //Apply filter
    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = FilterResults()
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredResults.values = if (filterPattern.isEmpty()) {
                    outerArray
                } else {
                    outerArray.filter {
                        it.publicName.lowercase().contains(filterPattern)
                    }
                }
                return filteredResults
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredClubs = results?.values as List<Client>
                notifyDataSetChanged()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_club_list, parent, false)
        return ClubViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        //val reslult=holder.bind(outerArray[position])
        val reslult =filteredClubs[position]
        holder.tvName.text = reslult.publicName
        holder.tvLable.text = reslult.county
        holder.cardIdLayout.setOnClickListener {
            Log.e("click.." , "clickClubItem")
            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }
        if (reslult.coverImagePath == null ) {
            Glide.with(holder.backgroundImage).load(R.drawable.resort_card_bg).into(holder.backgroundImage)
            //Glide.with(holder.background_image).load(Constants.BASE_URL_IMAGE).into(holder.background_image)
        } else {
            Glide.with(holder.backgroundImage).load(Constants.BASE_URL_IMAGE + reslult.coverImagePath).diskCacheStrategy(
                DiskCacheStrategy.ALL).fitCenter()
                .into(holder.backgroundImage)
        }
    }

    //override fun getItemCount(): Int = outerArray.size
    override fun getItemCount(): Int = filteredClubs.size
    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.lable1)
        val tvLable: TextView = itemView.findViewById(R.id.lable2)
        val cardIdLayout: LinearLayout = itemView.findViewById(R.id.cardIdLayout)
        val backgroundImage: ImageView = itemView.findViewById(R.id.clubBackgroundIdImage)
    }
}
