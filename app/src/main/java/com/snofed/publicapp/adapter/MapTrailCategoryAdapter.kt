package com.snofed.publicapp.adapter

import StatusItem
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.PoisTypeAdapter.OnItemSelectedListener
import com.snofed.publicapp.databinding.TrailCategoryListBinding


class MapTrailCategoryAdapter(private val trailList: List<StatusItem>,private val selectedZoneIds: List<String>,private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<MapTrailCategoryAdapter.TrailCategoryViewHolder>() {
   // private val selectedIds = mutableListOf<String>()
    private val selectedZone= selectedZoneIds.toMutableList()
    private var onItemSelectedListener: OnItemSelectedListener? = null


    inner class TrailCategoryViewHolder(private val binding: TrailCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trail: StatusItem, isSelected: Boolean) {

            binding.txtTrailCategory.text=  trail.text

            // Update background color based on selection
            binding.txtTrailCategory.setBackgroundColor(
                if (isSelected)
                    ContextCompat.getColor(binding.root.context, R.color.selected_background)
                else
                    ContextCompat.getColor(binding.root.context, R.color.default_background)  // Default background color
            )

            // Change the background when selected
            binding.txtTrailCategory.setBackgroundResource(
                if (isSelected) R.drawable.trail_selector_bg else R.drawable.text_outline
            )

            if (isSelected) {

                binding.txtTrailCategory.setTextColor(ContextCompat.getColor(binding.txtTrailCategory.context, R.color.selected_color))
            } else {
                binding.txtTrailCategory.setTextColor(ContextCompat.getColor(binding.txtTrailCategory.context, R.color.maps_btn))
            }

            // Log for debugging purposes
            Log.e("iconPathPraveen", "iconPathPraveen: ${trail.id}")
            Log.e("iconPathPraveen", "iconPathPraveen: ${trail.text}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailCategoryViewHolder {
        val binding = TrailCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailCategoryViewHolder, position: Int) {

        val trailType = trailList[position]
        val trailTypeId = trailType.id // Assume each item has a unique 'id'
        val isSelected = selectedZone.contains(trailTypeId) // Use position as the ID

        holder.bind(trailType, isSelected)

        // Handle item click
        holder.itemView.setOnClickListener {
            if (selectedZone.contains(trailTypeId)) {
                selectedZone.remove(trailTypeId)
            } else {
                selectedZone.add(trailTypeId)
            }

            val selectedIdsList = selectedZone.toList()  // Get selected IDs as a list
            onItemClick(selectedIdsList.joinToString(",")) // Call the item click callback
            notifyItemChanged(position)

            // Notify the listener when the selection changes
            onItemSelectedListener?.onItemsSelected(selectedZone)

        }
    }
    override fun getItemCount(): Int = trailList.size
}
