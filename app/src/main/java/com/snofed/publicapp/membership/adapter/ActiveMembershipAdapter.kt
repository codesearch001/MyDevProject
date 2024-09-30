package com.snofed.publicapp.membership.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.membership.model.BenefitResponse
import com.snofed.publicapp.membership.model.Daum


class ActiveMembershipAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<ActiveMembershipAdapter.ClubViewHolder>(){
    private var activeArray: List<Daum> = listOf()


    interface OnItemClickListener {
        fun onItemClick(id: String)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setFeed(clubs: List<Daum>?) {
        if (clubs != null) {
            this.activeArray = clubs
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.active_memb_list, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val reslult = activeArray[position]

        holder.txt_active_membership_name.text = reslult.membershipName

        if (reslult.isActive) {
            holder.txt_active_membership_status.text = "Active"
        } else {
            holder.txt_active_membership_status.text = ""
        }

        // Assuming Active membership has an 'id' property
        holder.cardid.setOnClickListener {
            listener.onItemClick(reslult.id)
        }
    }

    override fun getItemCount(): Int = activeArray.size// change for filter


    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_active_membership_name: TextView = itemView.findViewById(R.id.txt_active_membership_name)
        val txt_active_membership_title: TextView = itemView.findViewById(R.id.txt_active_membership_title)
        val txt_active_membership_status: TextView = itemView.findViewById(R.id.txt_active_membership_status)
        val cardid: CardView = itemView.findViewById(R.id.cardid)


    }
}

