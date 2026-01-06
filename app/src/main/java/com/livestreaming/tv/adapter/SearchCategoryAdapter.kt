package com.livestreaming.tv.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemCategoryBinding
import com.livestreaming.tv.utils.Category

class SearchCategoryAdapter(
    private val items: List<Category>,
    private val onItemClick: (List<Category>) -> Unit,
) : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>() {

    private var selectedPosition = mutableSetOf<Int>()

    inner class ViewHolder(
        val binding: ItemCategoryBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvCategory.text = item.name
        val isSelected = selectedPosition.contains(position)

        // Selected / Unselected UI
        if (isSelected) {
            holder.binding.cardCategory.setCardBackgroundColor(Color.WHITE)
            holder.binding.tvCategory.setTextColor(Color.BLACK)
        } else {
            holder.binding.cardCategory.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.binding.root.context,
                    R.color.white_9_percent
                )
            )
            holder.binding.tvCategory.setTextColor(Color.WHITE)
        }

        holder.binding.root.setOnClickListener {
            if (selectedPosition.contains(position)) {
                selectedPosition.remove(position)
            } else {
                selectedPosition.add(position)
            }
            notifyItemChanged(position)
            onItemClick(getSelectedItems())
        }
    }

    override fun getItemCount(): Int = items.size

    private fun getSelectedItems(): List<Category> {
        return selectedPosition.map { items[it] }
    }
}
