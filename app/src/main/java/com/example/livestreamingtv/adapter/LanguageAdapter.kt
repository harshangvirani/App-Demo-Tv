package com.example.livestreamingtv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamingtv.R
import com.example.livestreamingtv.databinding.ItemLanguageBinding
import com.example.livestreamingtv.ui.fragments.common.languages.LanguageSelectionFragment

class LanguageAdapter(
    private val languages: List<LanguageSelectionFragment.Language>,
    private val onLanguageSelected: (LanguageSelectionFragment.Language, Int) -> Unit,
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    
    private var selectedPosition = 2 // Default 3rd item (index 2)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position], position == selectedPosition)
    }
    
    override fun getItemCount(): Int = languages.size
    
    fun setSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            val oldPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(oldPosition, "SELECTION_UPDATE")
            notifyItemChanged(position, "SELECTION_UPDATE")
        }
    }
    
    fun getSelectedPosition(): Int = selectedPosition
    
    inner class LanguageViewHolder(
        private val binding: ItemLanguageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            // Touch/Mouse click
            binding.cvLanguageCard.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedPosition(position)
                    onLanguageSelected(languages[position], position)
                }
            }
            
            // TV Remote D-pad CENTER/ENTER
            binding.cvLanguageCard.setOnKeyListener { _, keyCode, event ->
                if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                    val position = bindingAdapterPosition
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                        android.view.KeyEvent.KEYCODE_ENTER -> {
                            if (position != RecyclerView.NO_POSITION) {
                                setSelectedPosition(position)
                                onLanguageSelected(languages[position], position)
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
        }
        
        fun bind(language: LanguageSelectionFragment.Language, isSelected: Boolean) {
            binding.tvLanguageName.text = language.name
            
            // Make card focusable for TV remote
            binding.cvLanguageCard.isFocusable = true
            binding.cvLanguageCard.isFocusableInTouchMode = true
            
            updateVisualState(isSelected, binding.cvLanguageCard.hasFocus())
            
            // Add focus listener for border effect on TV remote
            binding.cvLanguageCard.setOnFocusChangeListener { _, hasFocus ->
                updateVisualState(bindingAdapterPosition == selectedPosition, hasFocus)
            }
        }

        fun updateVisualState(isSelected: Boolean, hasFocus: Boolean) {
            val context = binding.root.context
            
            if (isSelected) {
                binding.cvLanguageCard.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.white)
                )
                binding.tvLanguageName.setTextColor(
                    ContextCompat.getColor(context, android.R.color.black)
                )
                binding.cvLanguageCard.strokeWidth = 0
            } else {
                val grayColor = ContextCompat.getColor(context, R.color.white)
                val alphaColor = (0.1f * 255).toInt() shl 24 or (grayColor and 0x00FFFFFF)
                binding.cvLanguageCard.setCardBackgroundColor(alphaColor)
                binding.tvLanguageName.setTextColor(
                    ContextCompat.getColor(context, android.R.color.white)
                )
                binding.cvLanguageCard.strokeWidth = 0
            }

            if (hasFocus) {
                binding.cvLanguageCard.strokeWidth = 0
                binding.cvLanguageCard.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(150)
                    .start()
            } else {
                binding.cvLanguageCard.strokeWidth = 0
                
                binding.cvLanguageCard.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(150)
                    .start()
            }
        }
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.updateVisualState(position == selectedPosition, holder.itemView.hasFocus())
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}
