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
import com.snofed.publicapp.models.membership.Benefit

import com.snofed.publicapp.utils.ServiceUtil


class BenefitsAdapter(private val benefitsList: List<Benefit>) : RecyclerView.Adapter<BenefitsAdapter.BenefitViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.membership_item_benefit, parent, false)
        return BenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val benefit = benefitsList[position]
        var benefitsVisible = position == 0 // Only the first item is open by default

        holder.tvBenefitName.text = benefit.name
        holder.tvBenefitDescription.text = benefit.description

        // Optionally, load images using Glide or similar if you have partner logos
        if (benefit.partnerLogoPath == null) {
            Glide.with(holder.profileImageView).load(R.drawable.logo).into(holder.profileImageView)
        } else {
            Glide.with(holder.profileImageView)
                .load(ServiceUtil.BASE_URL_MEMB_IMAGE +"/"+ benefit.partnerLogoPath).diskCacheStrategy(
                    DiskCacheStrategy.ALL).fitCenter().into(holder.profileImageView)
        }
        // Set visibility and icon based on the position (first item is open, others are closed)
        if (benefitsVisible) {
            holder.tvBenefitDescription.visibility = View.VISIBLE
            holder.view2.visibility = View.VISIBLE
            holder.upImageView.setImageResource(R.drawable.custom_open_drawable) // Icon for open state

            // Scroll to the first position if it's the default open position
            if (position == 0) {
                holder.itemView.post {
                    (holder.itemView.parent as RecyclerView).smoothScrollToPosition(0)
                }
            }

        } else {
            holder.tvBenefitDescription.visibility = View.GONE
            holder.view2.visibility = View.VISIBLE
            holder.upImageView.setImageResource(R.drawable.custom_close_drawable) // Icon for closed state
        }



        // Set click listener on the icon to toggle description visibility
        holder.upImageView.setOnClickListener {
            if (benefitsVisible) {
                // Hide the description
                holder.tvBenefitDescription.visibility = View.GONE
                holder.upImageView.setImageResource(R.drawable.custom_close_drawable) // Change icon to down arrow
            } else {
                // Show the description
                holder.tvBenefitDescription.visibility = View.VISIBLE
                holder.view2.visibility = View.VISIBLE
                holder.upImageView.setImageResource(R.drawable.custom_open_drawable) // Change icon to up arrow
            }
            benefitsVisible = !benefitsVisible // Toggle the flag
        }
       }

    override fun getItemCount(): Int {
        return benefitsList.size
    }


    // ViewHolder class that holds the views for each item
    class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBenefitName: TextView = itemView.findViewById(R.id.nameTextView)
        val tvBenefitDescription: TextView = itemView.findViewById(R.id.tvBenefitDescription)
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val upImageView: ImageView = itemView.findViewById(R.id.upImageView)
        val view2: View = itemView.findViewById(R.id.view2)
    }
}
