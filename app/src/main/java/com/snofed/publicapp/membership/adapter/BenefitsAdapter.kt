package com.snofed.publicapp.membership.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.membership.model.Benefit
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.ServiceUtil


class BenefitsAdapter(private val benefitsList: List<Benefit>) : RecyclerView.Adapter<BenefitsAdapter.BenefitViewHolder>() {

    // ViewHolder class that holds the views for each item
    class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBenefitName: TextView = itemView.findViewById(R.id.nameTextView)
        val tvBenefitDescription: TextView = itemView.findViewById(R.id.tvBenefitDescription)
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        // Optionally, include other views like an ImageView for logos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.membership_item_benefit, parent, false)
        return BenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val benefit = benefitsList[position]
        holder.tvBenefitName.text = benefit.name
        holder.tvBenefitDescription.text = benefit.description

        // Optionally, load images using Glide or similar if you have partner logos
        if (benefit.partnerLogoPath == null) {
            Glide.with(holder.profileImageView).load(R.drawable.logo)
                .into(holder.profileImageView)
        } else {
            Glide.with(holder.profileImageView)
                .load(ServiceUtil.BASE_URL_MEMB_IMAGE +"/"+ benefit.partnerLogoPath).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).fitCenter()
                .into(holder.profileImageView)
        }
    }

    override fun getItemCount(): Int {
        return benefitsList.size
    }
}
