package com.snofed.publicapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.ItemCategoryBinding
import com.snofed.publicapp.databinding.ItemSubItemBinding
import com.snofed.publicapp.ui.help.Category

class ExpandableAdapter(private val categories: List<Category>) : RecyclerView.Adapter<ExpandableAdapter.CategoryViewHolder>() {

    private var expandedPosition = -1

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val category = categories[position]
        holder.binding.categoryTitle.text = category.title

        val isExpanded = position == expandedPosition
        holder.binding.itemsLayout.removeAllViews()
        holder.binding.itemsLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.binding.expandIcon.setImageResource(
            if (isExpanded) R.drawable.up else R.drawable.down
        )

        if (isExpanded) {
            category.items.forEach { item ->
                val itemBinding = ItemSubItemBinding.inflate(LayoutInflater.from(holder.binding.root.context), holder.binding.itemsLayout, false)
                itemBinding.subItemText.text = item
                holder.binding.itemsLayout.addView(itemBinding.root)
            }
        }

        holder.binding.root.setOnClickListener {
            val previousExpandedPosition = expandedPosition
            if (isExpanded) {
                expandedPosition = -1
                notifyItemChanged(position)
            } else {
                expandedPosition = position
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = categories.size
}

