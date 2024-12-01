package com.snofed.publicapp.adapter

import StatusItem
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.PoisTypeAdapter.OnItemSelectedListener
import com.snofed.publicapp.databinding.ZonesTypeListBinding


class ZonesTypeAdapter(private val zonesList: List<StatusItem>,private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<ZonesTypeAdapter.TrailCategoryViewHolder>() {
    private val selectedIds = mutableListOf<String>()
    private var onItemSelectedListener: OnItemSelectedListener? = null
    inner class TrailCategoryViewHolder(private val binding: ZonesTypeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(zone: StatusItem,isSelected: Boolean) {
            // Assuming you have an ImageView or similar to load the icon
            // Load the icon from the path into an ImageView
            binding.txtZoneType.text=  zone.text
            // Update background color based on selection
            binding.txtZoneType.setBackgroundColor(
                if (isSelected)
                    ContextCompat.getColor(binding.root.context, R.color.selected_background)
                else
                    ContextCompat.getColor(binding.root.context, R.color.default_background)  // Default background color
            )

            // Change the background when selected
            binding.txtZoneType.setBackgroundResource(
                if (isSelected) R.drawable.trail_selector_bg else R.drawable.text_outline
            )

            if (isSelected) {

                binding.txtZoneType.setTextColor(ContextCompat.getColor(binding.txtZoneType.context, R.color.selected_color))
            } else {
                binding.txtZoneType.setTextColor(ContextCompat.getColor(binding.txtZoneType.context, R.color.maps_btn))
            }
            // Log for debugging purposes
            Log.e("iconPathPraveen", "iconPathPraveen: ${zone.id}")
            Log.e("iconPathPraveen", "iconPathPraveen: ${zone.text}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailCategoryViewHolder {
        val binding = ZonesTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailCategoryViewHolder, position: Int) {
        //holder.bind(zonesList[position])
        val zoneType = zonesList[position]
        val zoneTypeId = zoneType.id // Assume each item has a unique 'id'
        val isSelected = selectedIds.contains(zoneTypeId) // Use position as the ID

        holder.bind(zoneType, isSelected)

        // Handle item click
        holder.itemView.setOnClickListener {
            if (selectedIds.contains(zoneTypeId)) {
                selectedIds.remove(zoneTypeId)
            } else {
                selectedIds.add(zoneTypeId)
            }

            val selectedIdsList = selectedIds.toList()  // Get selected IDs as a list
            onItemClick(selectedIdsList.joinToString(",")) // Call the item click callback
            notifyItemChanged(position)

            // Notify the listener when the selection changes
            onItemSelectedListener?.onItemsSelected(selectedIds)
        }
    }

    override fun getItemCount(): Int = zonesList.size
}