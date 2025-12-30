package com.example.livestreamingtv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamingtv.databinding.ItemSliderImageBinding
class ImageSliderAdapter(
    private val images: List<Int>
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemSliderImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Use modulo to create infinite loop effect
        val actualPosition = position % images.size
        holder.bind(images[actualPosition])
    }
    
    override fun getItemCount(): Int = images.size // Reasonable large number for infinite effect
    
    class ImageViewHolder(
        private val binding: ItemSliderImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.ivSliderImage.setImageResource(imageRes)
            
            // Handle focus state for TV navigation
            binding.cardSlider.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.cardSlider.strokeWidth = 4 // 4dp border
                } else {
                    binding.cardSlider.strokeWidth = 0 // No border
                }
            }
        }
    }
}
