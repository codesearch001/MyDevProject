package com.snofed.publicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.adapter.ClubActionAdapter.GridViewHolder
import com.snofed.publicapp.databinding.ActionItemsBinding
import com.snofed.publicapp.databinding.GalleryListItemsBinding
import com.snofed.publicapp.databinding.RectangularItemLayoutBinding


/*class GalleryAdapter {}*/
class GalleryAdapter(private val itemList: List<MyItem>) : RecyclerView.Adapter<GalleryAdapter.GridViewHolder>() {


    inner class GridViewHolder(val binding: GalleryListItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = itemList[adapterPosition]
                //listener.onItemClick(item.id)
                // Example: Show a toast message
                // ToastUtils.showToast(context, "Item clicked with id: ${item.id}")
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = GalleryListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)

    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.imageViewGrid.setImageResource(item.gridImageResId)

    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    // Example data class representing your items
    data class MyItem(val gridImageResId: Int)
}





