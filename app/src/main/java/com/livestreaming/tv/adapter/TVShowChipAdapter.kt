package com.livestreaming.tv.adapter

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemChipTvShowCategoryBinding
import com.livestreaming.tv.utils.Category

class TVShowChipAdapter() : RecyclerView.Adapter<TVShowChipAdapter.TVShowChipViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val chipsList = AsyncListDiffer(this, diffUtil)
    var onItemClick: ((Int, String) -> Unit)? = null
    private var selectionItemPosition: Int = 0 // keeps track of selected chip

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TVShowChipViewHolder = TVShowChipViewHolder(
        ItemChipTvShowCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: TVShowChipViewHolder,
        position: Int,
    ) {
        holder.bind(chipsList.currentList[position].name, position)
    }

    override fun getItemCount(): Int = chipsList.currentList.size

    inner class TVShowChipViewHolder(private val binding: ItemChipTvShowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(string: String, position: Int) {
            binding.tvChipName.text = string

            // Set initial style based on selection
            updateChipStyle(position == selectionItemPosition)

            // Focus listener for remote navigation
            binding.cvChip.setOnFocusChangeListener { _, hasFocus ->
                updateChipStyle(hasFocus || position == selectionItemPosition)
            }

           binding.cvChip.setOnClickListener {
               // Update selection
               val previousPosition: Int = selectionItemPosition
               selectionItemPosition = position
               notifyItemChanged(previousPosition) // refresh old chip
               notifyItemChanged(selectionItemPosition) // refresh new chip

               // Trigger callback
               onItemClick?.invoke(position, string)
           }
        }

        // Helper function to style the chip
        private fun updateChipStyle(selected: Boolean) {
            if (selected) {
                binding.cvChip.strokeWidth = 2
                binding.cvChip.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.gray)
                )
            } else {
                binding.cvChip.strokeWidth = 0
                binding.cvChip.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white_9_percent)
                )
            }
        }
    }
}
