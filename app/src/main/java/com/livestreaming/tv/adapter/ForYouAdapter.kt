package com.livestreaming.tv.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemForYouRvBinding
import com.livestreaming.tv.utils.ImagesItem

class ForYouAdapter(
    private val contentList: List<ImagesItem>,
    private val onContentClick: (Int, ImagesItem) -> Unit,
) : RecyclerView.Adapter<ForYouAdapter.ContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = ItemForYouRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(contentList[position], position)
    }

    override fun getItemCount(): Int = contentList.size

    inner class ContentViewHolder(
        private val binding: ItemForYouRvBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgPoster.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContentClick(position, contentList[position])
                }
            }
        }

        fun bind(posterImage: ImagesItem, position: Int) {
            binding.imgPoster.setImageResource(posterImage.img)
            binding.tvNumber.text = "${position + 1}"

            // Add scale animation on focus
            binding.imgPoster.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                    binding.imgPoster.apply {
                        strokeWidth = 2f
                        setBackgroundResource(R.drawable.card_white_border)
                    }

                } else {
                    //view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                    binding.imgPoster.apply {
                        strokeWidth = 0f
                        setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
        }
    }
}
