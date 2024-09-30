package com.snofed.publicapp.membership.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.models.membership.Benefit
import com.snofed.publicapp.utils.ServiceUtil

class SupportMembershipAdapterr(private var benefitsList: List<Benefit>,private val listener: OnItemClickListener) : RecyclerView.Adapter<SupportMembershipAdapterr.BenefitViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(
            id: String,
            name: String,
            description: String,
            partnerLogoPath: String,
            partnerWebLink: String
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<Benefit>?) {
        if (clubs != null) {
            this.benefitsList = clubs
        }
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.support_membership_list, parent, false)
        return BenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val benefit = benefitsList[position]
        var benefitsVisible = position == 0 // Only the first item is open by default

        holder.trade_in_title.text = benefit.name
        holder.skotersv_description.text = benefit.description

        // Optionally, load images using Glide or similar if you have partner logos
        if (benefit.partnerLogoPath == null) {
            Glide.with(holder.img_logo).load(R.drawable.logo).into(holder.img_logo)
        } else {
            Glide.with(holder.img_logo).load(ServiceUtil.BASE_URL_MEMB_IMAGE +"/"+ benefit.partnerLogoPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(holder.img_logo)
        }

        // Assuming Active membership has an 'id' property
        holder.card1.setOnClickListener {
            listener.onItemClick(benefit.id,benefit.name,benefit.description,benefit.partnerLogoPath,benefit.partnerWebLink)
        }

    }

    override fun getItemCount(): Int {
        return benefitsList.size
    }


    // ViewHolder class that holds the views for each item
    class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trade_in_title: TextView = itemView.findViewById(R.id.trade_in_title)
        val skotersv_description: TextView = itemView.findViewById(R.id.skotersv_description)
        val img_logo: ImageView = itemView.findViewById(R.id.img_logo)
        val card1: CardView = itemView.findViewById(R.id.card1)
    }
}