package com.snofed.publicapp.adapter

import StatusItem
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationBarView
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.PoisTypeListBinding

/*class PoisTypeAdapter(private val poiList: List<String>,) : RecyclerView.Adapter<PoisTypeAdapter.PoisTypeViewHolder>() {
    private val selectedIds = mutableListOf<Int>() // Store selected IDs
    inner class PoisTypeViewHolder(private val binding: PoisTypeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(iconPath: String) {
            // Assuming you have an ImageView or similar to load the icon
            // Load the icon from the path into an ImageView
            binding.imgPoisType.setImageURI(Uri.parse(iconPath))
            // Use Glide to load the image from the provided path
            Glide.with(binding.imgPoisType.context)
                .load(iconPath) // Load the image URL/path
                .placeholder(R.drawable.dinner) // Optional: Placeholder while loading
                .error(R.drawable.dinner) // Optional: Error image if load fails
                .into(binding.imgPoisType) // Set the image into the ImageView

            // Log for debugging purposes
            Log.e("iconPathPraveen", "iconPathPraveen: $iconPath")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoisTypeViewHolder {
        val binding = PoisTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoisTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoisTypeViewHolder, position: Int) {
        holder.bind(poiList[position])

        // Handle item click
        holder.itemView.setOnClickListener {
            if (selectedIds.contains(item.id)) {
                selectedIds.remove(item.id)
            } else {
                selectedIds.add(item.id)
            }
            onItemClick(item.id)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = poiList.size
}*/

class PoisTypeAdapter(private val poiList: List<StatusItem>, private val selectedIds: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<PoisTypeAdapter.PoisTypeViewHolder>() {

    //private val selectedIds = mutableListOf<String>()
    private val selected= selectedIds.toMutableList()
    private var onItemSelectedListener: OnItemSelectedListener? = null

    inner class PoisTypeViewHolder(private val binding: PoisTypeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(iconPath: String, isSelected: Boolean) {
            // Load the icon into the ImageView
            Glide.with(binding.imgPoisType.context)
                .load(iconPath) // Load the image URL/path
                .placeholder(R.drawable.dinner) // Placeholder while loading
                .error(R.drawable.dinner) // Error image if loading fails
                .into(binding.imgPoisType) // Set image in ImageView

            // Update background color based on selection
            binding.imgPoisType.setBackgroundColor(
                if (isSelected)
                    ContextCompat.getColor(binding.root.context, R.color.selected_background)
                else
                    ContextCompat.getColor(binding.root.context, R.color.default_background)  // Default background color
            )

            // Change the background when selected
            binding.imgPoisType.setBackgroundResource(
                if (isSelected) R.drawable.selected_background else R.drawable.outline
            )

            // Change icon color using tint
            if (isSelected) {
                binding.imgPoisType.setColorFilter(Color.parseColor("#FFFFFF")) // Change to selected color (Pink)
            } else {
                binding.imgPoisType.clearColorFilter() // Remove color filter when not selected
            }

            // Log for debugging purposes
            Log.e("iconPathPraveen", "iconPathPraveen: $iconPath")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoisTypeViewHolder {
        val binding = PoisTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoisTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoisTypeViewHolder, position: Int) {
        val poisType = poiList[position]
        val poisTypeId = poisType.id // Assume each item has a unique 'id'
        val isSelected = selected.contains(poisTypeId) // Use position as the ID

        holder.bind(poisType.iconPath!!, isSelected)

        // Handle item click
        holder.itemView.setOnClickListener {
            if (selected.contains(poisTypeId)) {
                selected.remove(poisTypeId)
            } else {
                selected.add(poisTypeId)
            }

            val selectedIdsList = selected.toList()  // Get selected IDs as a list
            onItemClick(selectedIdsList.joinToString(",")) // Call the item click callback
            notifyItemChanged(position)
            onItemSelectedListener?.onItemsSelected(selected)

            /*// Notify the listener when the selection changes
            onItemSelectedListener?.onItemsSelected(selectedIds)*/

        }
    }

    override fun getItemCount(): Int = poiList.size

    interface OnItemSelectedListener {
        fun onItemsSelected(selectedIds: List<String>)
    }

    // Public method to retrieve selected IDs
    fun getSelectedIds(): List<String> = selectedIds

}
