package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemChipTvShowCategoryBinding
import com.livestreaming.tv.utils.Category

class MovieCategoryAdapter() :
    RecyclerView.Adapter<MovieCategoryAdapter.MovieCategoryViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val categoryList = AsyncListDiffer(this, diffUtil)
    var onClickItem: ((Category) -> Unit)? = null
    private var selectionItemPosition: Int = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieCategoryViewHolder = MovieCategoryViewHolder(
        ItemChipTvShowCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(
        holder: MovieCategoryViewHolder,
        position: Int,
    ) {
        holder.bind(categoryList.currentList[position], position == selectionItemPosition)
    }

    override fun getItemCount(): Int = categoryList.currentList.size

    inner class MovieCategoryViewHolder(private val binding: ItemChipTvShowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, isSelected: Boolean) {
            binding.tvChipName.text = category.name

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
                onClickItem?.invoke(category)
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