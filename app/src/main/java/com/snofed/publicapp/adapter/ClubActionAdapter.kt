package com.snofed.publicapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.ActionItemsBinding
import com.snofed.publicapp.models.browseclubaction.GridItem
import com.snofed.publicapp.utils.OnItemClickListener
import com.snofed.publicapp.utils.ToastUtils



/*
class ClubActionAdapter {
}*/
class ClubActionAdapter( private val items: List<GridItem>, private val listener: OnItemClickListener,  private val context: Context) : RecyclerView.Adapter<ClubActionAdapter.GridViewHolder>() {

    /*class GridViewHolder(val binding: ActionItemsBinding) : RecyclerView.ViewHolder(binding.root)*/
   /* interface OnItemClickListener {
        fun onItemClick(itemId: Int)
    }*/

    inner class GridViewHolder(val binding: ActionItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = items[adapterPosition]
                listener.onItemClick(item.id)
                // Example: Show a toast message
              //  ToastUtils.showToast(context, "Item clicked with id: ${item.id}")
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ActionItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)

    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = items[position]
        holder.binding.actionText.text = item.title
        holder.binding.actionImg.setImageResource(item.imageResId)
        holder.binding.backgroundimage.setBackgroundResource(item.backgroundImageResId)

    }

    override fun getItemCount(): Int = items.size
}
