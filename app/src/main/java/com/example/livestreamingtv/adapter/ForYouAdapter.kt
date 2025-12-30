package com.example.livestreamingtv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamingtv.databinding.ItemContentPosterBinding

class ForYouAdapter(
    private val contentList: List<Int>,
    private val onContentClick: (Int) -> Unit
) : RecyclerView.Adapter<ForYouAdapter.ContentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = ItemContentPosterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(contentList[position])
    }

    override fun getItemCount(): Int = contentList.size
    
    inner class ContentViewHolder(
        private val binding: ItemContentPosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContentClick(position)
                }
            }
            
            // TV Remote D-pad key handling
            binding.root.setOnKeyListener { view, keyCode, event ->
                if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                        android.view.KeyEvent.KEYCODE_ENTER -> {
                            val position = bindingAdapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                onContentClick(position)
                            }
                            true
                        }
                        // Let D-pad UP propagate to MainActivity for tab bar navigation
                        android.view.KeyEvent.KEYCODE_DPAD_UP -> false
                        else -> false
                    }
                } else {
                    false
                }
            }
            
            // Add focus listener for TV remote
            binding.root.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(200)
                        .start()
                } else {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(200)
                        .start()
                }
            }
        }
        
        fun bind(imageRes: Int) {
            binding.ivContentPoster.setImageResource(imageRes)
            binding.root.isFocusable = true
            binding.root.isFocusableInTouchMode = true
            binding.root.isClickable = true
        }
    }
}
