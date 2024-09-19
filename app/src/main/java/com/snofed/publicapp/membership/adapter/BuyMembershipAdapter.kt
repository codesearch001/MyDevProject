package com.snofed.publicapp.membership.adapter

import android.annotation.SuppressLint
import android.os.Build

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.membership.model.BuyMembershipResponse
import java.util.Locale

class BuyMembershipAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<BuyMembershipAdapter.ClubViewHolder>(),
    Filterable {
    private var feedArray: List<BuyMembershipResponse> = listOf()

    private var filteredFeedArray: List<BuyMembershipResponse> = listOf()

    init {
        filteredFeedArray = feedArray // Initially set the filtered list to the full list
    }

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<BuyMembershipResponse>?) {
        if (clubs != null) {
            this.feedArray = clubs
            this.filteredFeedArray =  this.feedArray // Update both lists when setting data
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_list, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val reslult = feedArray[position]
        var benefitsVisible = false // Initially benefits are hidden

        holder.txt_membership_title.text = reslult.name
        holder.txt_membership_price.text = reslult.priceWithCurrency
        holder.txt_benefits_description.text = reslult.description

        holder.become_member_button.setOnClickListener {
            //Log.e("click..", "clickClubItem" + reslult.id)
            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }

        // Set the initial text and icon for the "Show benefits" button
        if (reslult.benefits.isNotEmpty()) {
            holder.btn_Buy_Memberships.text = "Show benefits (${reslult.benefits.size})"
            holder.btn_Buy_Memberships.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.custom_drawable,
                0
            )
            holder.btn_Buy_Memberships.isClickable = true
            holder.btn_Buy_Memberships.isEnabled = true
        } else {
            // holder.btn_Buy_Memberships.text = "No benefits available"
            holder.btn_Buy_Memberships.text = "Show benefits (${reslult.benefits.size})"
            holder.btn_Buy_Memberships.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                0,
                0
            ) // No icon
            holder.btn_Buy_Memberships.isClickable = false
            holder.btn_Buy_Memberships.isEnabled = false
        }

        // Set up the benefits RecyclerView if there are benefits
        val benefitsAdapter = BenefitsAdapter(reslult.benefits)
        holder.benefitsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.benefitsRecyclerView.adapter = benefitsAdapter

        // Initially hide the benefits RecyclerView and the separator view
        holder.benefitsRecyclerView.visibility = View.GONE
        holder.view2.visibility = View.GONE

        // Set click listener to toggle between "Show benefits" and "Hide benefits"
        holder.btn_Buy_Memberships.setOnClickListener {
            if (benefitsVisible) {
                // Hide the benefits
                holder.benefitsRecyclerView.visibility = View.GONE
                holder.view2.visibility = View.GONE
                holder.btn_Buy_Memberships.text = "Show benefits (${reslult.benefits.count()})"
                holder.btn_Buy_Memberships.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.custom_up_drawable, 0)
                // Optionally, hide the benefits RecyclerView or details here
            } else {
                // Show the benefits
                holder.benefitsRecyclerView.visibility = View.VISIBLE
                holder.view2.visibility = View.VISIBLE
                holder.btn_Buy_Memberships.text = "Hide benefits (${reslult.benefits.count()})"
                holder.btn_Buy_Memberships.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.custom_drawable, 0)
                // Optionally, show the benefits RecyclerView or details here
            }

            // Scroll to the bottom of the RecyclerView
            holder.benefitsRecyclerView.post {
                holder.benefitsRecyclerView.smoothScrollToPosition(benefitsAdapter.itemCount - 1)
                holder.benefitsRecyclerView.requestLayout()
            }
            // Toggle the benefitsVisible flag
            benefitsVisible = !benefitsVisible
        }
        /*   if (reslult.benefitsNames.isNotEmpty()){
               holder.btnBuyMemberships.text = reslult.benefitsNames.joinToString(", ")
           }else{
               holder.btnBuyMemberships.isVisible = false
           }*/


    }

    //override fun getItemCount(): Int = feedArray.size
    override fun getItemCount(): Int = filteredFeedArray.size// change for filter


    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_membership_title: TextView = itemView.findViewById(R.id.txt_membership_title)
        val txt_membership_price: TextView = itemView.findViewById(R.id.txt_membership_price)
        val txt_benefits_description: TextView = itemView.findViewById(R.id.txt_benefits_description)
        val btn_Buy_Memberships: Button = itemView.findViewById(R.id.btn_Buy_Memberships)
        val become_member_button: Button = itemView.findViewById(R.id.become_member_button)
        val benefitsRecyclerView: RecyclerView = itemView.findViewById(R.id.benefitsRecyclerView)
        val view2: View = itemView.findViewById(R.id.view2)
    }

    // Implement the filtering method
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.values = feedArray // No filter applied, return original list
                } else {
                    val query = constraint.toString().lowercase().trim()
                    val filtered = feedArray.filter {
                        it.name.lowercase().contains(query) ||  it.name.uppercase().contains(query)||
                                it.priceWithCurrency.lowercase().contains(query)||
                                it.priceWithCurrency.uppercase().contains(query)||
                                it.description.lowercase().contains(query)||
                                it.description.uppercase().contains(query)
                    }
                    filterResults.values = filtered
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredFeedArray = if (results?.values == null) {
                    listOf()
                } else {
                    results.values as List<BuyMembershipResponse>
                }
                notifyDataSetChanged()
            }
        }
    }
}