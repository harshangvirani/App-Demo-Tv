package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.databinding.ItemSliderImageBinding
import com.livestreaming.tv.utils.HeroItem


class ImageSliderAdapter(
    private val items: List<HeroItem>
) : RecyclerView.Adapter<ImageSliderAdapter.Holder>() {

    inner class Holder(
        private val binding: ItemSliderImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HeroItem) {
            binding.ivSliderImage.setImageResource(item.bgImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSliderImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}


