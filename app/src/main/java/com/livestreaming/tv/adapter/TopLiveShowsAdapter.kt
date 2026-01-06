package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.databinding.ItemTopLiveShowBinding
import com.livestreaming.tv.utils.ImagesItem

class TopLiveShowsAdapter(
    private val contentList: List<ImagesItem>,
    private val onContentClick: (Int,ImagesItem) -> Unit,
) : RecyclerView.Adapter<TopLiveShowsAdapter.ContentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = ItemTopLiveShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(contentList[position],position)
    }

    override fun getItemCount(): Int = contentList.size

    inner class ContentViewHolder(
        private val binding: ItemTopLiveShowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContentClick(position,contentList[position])
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

        fun bind(imageRes: ImagesItem, position: Int) {
            binding.ivContentPoster.setImageResource(imageRes.img)
            binding.root.isFocusable = true
            binding.root.isFocusableInTouchMode = true
            binding.root.isClickable = true
        }
    }
}