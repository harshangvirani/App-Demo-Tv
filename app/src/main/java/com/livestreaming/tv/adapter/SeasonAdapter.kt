package com.livestreaming.tv.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemSeasonTextBinding
import com.livestreaming.tv.utils.Category

class SeasonAdapter() : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val seasonList = AsyncListDiffer(this, diffUtil)
    var onClick: ((Category) -> Unit)? = null
    private var selectionItemPosition: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SeasonViewHolder = SeasonViewHolder(
        ItemSeasonTextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: SeasonViewHolder,
        position: Int,
    ) {
        holder.bind(seasonList.currentList[position], position)
    }

    override fun getItemCount(): Int = seasonList.currentList.size

    inner class SeasonViewHolder(private val binding: ItemSeasonTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, position: Int) {
            binding.tvSeasonTxt.text = category.name

            updateText(position == selectionItemPosition)
            if (selectionItemPosition == 0) {
                onClick?.invoke(category)
            }

            binding.tvSeasonTxt.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.tvSeasonTxt.apply {
                        setBackgroundResource(R.drawable.bg_input_border_focused)
                        setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                    }
                } else {
                    binding.tvSeasonTxt.apply {
                        setBackgroundColor(Color.TRANSPARENT)
                        setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.light_gray
                            )
                        )
                    }

                }
            }

            binding.tvSeasonTxt.setOnClickListener {
                binding.tvSeasonTxt.setTextColor(Color.RED)
                onClick?.invoke(category)
            }
        }

        private fun updateText(isSelected: Boolean) {
            if (isSelected) {
                binding.tvSeasonTxt.setTextColor(Color.RED)
            } else {
                binding.tvSeasonTxt.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.light_gray
                    )
                )
            }
        }
    }
}