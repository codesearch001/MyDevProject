package com.snofed.publicapp.adapter

import StatusItem
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.ItemStatusBinding

/*
class MapIntervalAdapter {
}*/

/*
class MapIntervalAdapter(
    private val items: List<StatusItem>
) : RecyclerView.Adapter<MapIntervalAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelTextView: TextView = view.findViewById(R.id.labelTextView)
        val colorView: View = view.findViewById(R.id.colorView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val item = items[position]
        holder.labelTextView.text = item.text

        // Set the color dynamically
        try {
            holder.colorView.setBackgroundColor(Color.parseColor("#${item.color}"))
        } catch (e: IllegalArgumentException) {
            holder.colorView.setBackgroundColor(Color.GRAY) // Default color if parsing fails
        }
    }

    override fun getItemCount(): Int = items.size
}
*/


class MapIntervalAdapter(private val items: List<StatusItem>) : RecyclerView.Adapter<MapIntervalAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(private val binding: ItemStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StatusItem) {
            // Bind the data to the views
            binding.statusText.text = item.text

            // Set the background color dynamically
            // Set the background color dynamically
            try {
                // Attempt to parse the color from item.color
                val color = Color.parseColor(item.color)
                binding.colorIndicator.setBackgroundColor(color)
            } catch (e: IllegalArgumentException) {
                // If parsing fails, set a default color (e.g., white or any fallback color)
                binding.colorIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)


    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}

