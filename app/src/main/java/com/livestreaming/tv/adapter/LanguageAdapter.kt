package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemLanguageBinding
import com.livestreaming.tv.ui.fragments.common.languages.LanguageSelectionFragment

class LanguageAdapter(
    private val languages: List<LanguageSelectionFragment.Language>,
    private val onLanguageSelected: (LanguageSelectionFragment.Language, Int) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private var selectedPosition = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = languages.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cvLanguageCard.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedPosition(position)
                    onLanguageSelected(languages[position], position)
                }
            }
        }

        fun bind(language: LanguageSelectionFragment.Language, isSelected: Boolean) {
            binding.tvLanguageName.text = language.name

            // Highlight selected item
            if (isSelected) {
                binding.cvLanguageCard.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                binding.tvLanguageName.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.black)
                )
            } else {
                val grayColor = ContextCompat.getColor(binding.root.context, R.color.white)
                val alphaColor = (0.1f * 255).toInt() shl 24 or (grayColor and 0x00FFFFFF)
                binding.cvLanguageCard.setCardBackgroundColor(alphaColor)
                binding.tvLanguageName.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.white)
                )
            }
        }
    }
}
