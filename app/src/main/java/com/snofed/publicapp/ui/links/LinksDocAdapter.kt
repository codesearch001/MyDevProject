package com.snofed.publicapp.ui.links

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.models.browseSubClub.Link

class LinksDocAdapter() : RecyclerView.Adapter<LinksDocAdapter.ClubViewHolder>() {

    private var linksArray: List<Link> = listOf()

    interface OnItemClickListener {
        fun onItemClick(eventId: String, link: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(links: List<Link>?) {
        if (links != null) {
            this.linksArray = links
        }
        //Log.i("test","sizearr "+outerArray.size)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.links_doc_list, parent, false)
        return ClubViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val result = linksArray[position]
        holder.textEventTrailsName.text = result.linkName
        holder.textEventDate.text = result.link

        holder.linkDoc.setOnClickListener {
            //Log.e("click..", "clickClubItem " + result.id)

            // Open the link in an external browser
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(result.link)
            }
            holder.itemView.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = linksArray.size

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEventTrailsName: TextView = itemView.findViewById(R.id.link_title)
        val textEventDate: TextView = itemView.findViewById(R.id.url_links)
        val linkDoc: CardView = itemView.findViewById(R.id.linkDoc)
//        val imgActivitesIcon: ImageView = itemView.findViewById(R.id.imgActivitesIcon)
    }
}