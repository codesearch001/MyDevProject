package com.snofed.publicapp.adapter

import RealmRepository
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.dto.SubscribeDTO
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.ServiceUtil
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.SnofedUtils


class BrowseClubListAdapter(private val context: Context, private val listener: OnItemClickListener, private val clubViewModel: AuthViewModel) : RecyclerView.Adapter<BrowseClubListAdapter.ClubViewHolder>() {

    //private var clubs: List<NewClubData> = listOf()
    private var outerArray: List<Client> = listOf()
    private var filteredClubs: List<Client> = listOf()
    //private val wishlistItems: MutableSet<String> = mutableSetOf()

    interface OnItemClickListener {
        fun onItemClick(clientId: String)
        fun onWishlistClick(clientId: String) // Callback for wishlist icon clicks
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
        var favRefferal : Boolean = false;
        val reslult = filteredClubs[position]
        holder.clientRating.text = reslult.clientRating.toString()
        holder.totalRatings.text = "(" + reslult.totalRatings.toString() + ")"
        holder.tvName.text = reslult.publicName
        holder.tvLable.text = reslult.county

        holder.cardIdLayout.setOnClickListener {
            Log.e("click..", "clickClubItem")
            listener.onItemClick(reslult.id) // Assuming Client has an 'id' property
        }
        // Set wishlist icon based on the wishlist state

        if (reslult.isInWishlist == true) {
            holder.imgIdWishlist.setImageResource(R.drawable.hearth_filled)
            favRefferal = true
        } else {
            holder.imgIdWishlist.setImageResource(R.drawable.hearth_empty)
            favRefferal = false
        }

        // Handle wishlist icon click
        holder.imgIdWishlist.setOnClickListener {
            val clientId = reslult.id
            favRefferal = !favRefferal // Toggle the referral state
            if (favRefferal) {
                holder.imgIdWishlist.setImageResource(R.drawable.hearth_filled)
                val userId = AppPreference.getPreference(context, SharedPreferenceKeys.USER_USER_ID).toString()
                val subscribeDTO : SubscribeDTO = SubscribeDTO(
                    clientId = clientId,
                    publicUserId = userId,
                    subscribeDate = SnofedUtils.getDateNow(SnofedConstants.DATETIME_SERVER_FORMAT)
                )
                // call subscribe api
                clubViewModel.subscribeClubService(subscribeDTO)

                // update userRealm
                val realmRepository = RealmRepository()
                val userViewModelRealm = UserViewModelRealm(realmRepository)
                val realm = realmRepository.getRealmInstance() // Get a Realm instance from your repository

                realm.executeTransaction { transactionRealm ->
                    val userRealm = userViewModelRealm.getUserById(userId!!)
                    userRealm?.let {
                        it.favouriteClients?.add(clientId)
                        transactionRealm.insertOrUpdate(it) // Save the updated object
                    }
                }

            } else {
                holder.imgIdWishlist.setImageResource(R.drawable.hearth_empty)
                val userId = AppPreference.getPreference(context, SharedPreferenceKeys.USER_USER_ID).toString()
                val subscribeDTO : SubscribeDTO = SubscribeDTO(
                    clientId = clientId,
                    publicUserId = userId,
                    subscribeDate = SnofedUtils.getDateNow(SnofedConstants.DATETIME_SERVER_FORMAT)
                )
                // call unsubscribe api
               clubViewModel.unsubscribeClubService(subscribeDTO)

                // update userRealm
                val realmRepository = RealmRepository()
                val userViewModelRealm = UserViewModelRealm(realmRepository)
                val realm = realmRepository.getRealmInstance() // Get a Realm instance from your repository

                realm.executeTransaction { transactionRealm ->
                    val userRealm = userViewModelRealm.getUserById(userId!!)
                    userRealm?.let {
                        it.favouriteClients?.remove(clientId)
                        transactionRealm.insertOrUpdate(it) // Save the updated object
                    }
                }
            }
            listener.onWishlistClick(clientId) // Notify the listener of the change
        }

        if (reslult.coverImagePath == null) {
            Glide.with(holder.backgroundImage).load(R.drawable.resort_card_bg)
                .into(holder.backgroundImage)
            //Glide.with(holder.background_image).load(Constants.BASE_URL_IMAGE).into(holder.background_image)
        } else {
            Glide.with(holder.backgroundImage)
                .load(ServiceUtil.BASE_URL_IMAGE + reslult.coverImagePath).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).fitCenter()
                .into(holder.backgroundImage)
        }
    }

    //override fun getItemCount(): Int = outerArray.size
    override fun getItemCount(): Int = filteredClubs.size
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
