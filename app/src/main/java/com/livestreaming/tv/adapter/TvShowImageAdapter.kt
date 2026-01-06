package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemTvShowListBinding
import com.livestreaming.tv.utils.ImagesItem

class TvShowImageAdapter() : RecyclerView.Adapter<TvShowImageAdapter.TVShowImageViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ImagesItem>() {
        override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val imgList = AsyncListDiffer(this, diffUtil)
    var onClick: ((Int, ImagesItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TVShowImageViewHolder = TVShowImageViewHolder(
        ItemTvShowListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: TVShowImageViewHolder,
        position: Int,
    ) {
        holder.bind(position, imgList.currentList[position])
    }

    override fun getItemCount(): Int = imgList.currentList.size

    inner class TVShowImageViewHolder(private val binding: ItemTvShowListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardMovie.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                    binding.cardMovie.apply {
                        strokeWidth = 2
                        strokeColor = ContextCompat.getColor(binding.root.context, R.color.white)
                    }
                } else {
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                    binding.cardMovie.apply {
                        strokeWidth = 2
                        strokeColor =
                            ContextCompat.getColor(binding.root.context, R.color.card_background)
                    }
                }

            }
        }

        fun bind(position: Int, item: ImagesItem) {
            binding.imgPoster.setImageResource(item.img)

            //Focus Listen RecyclerViewItem
            binding.cardMovie.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.cardMovie.setCardBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                } else {
                    binding.cardMovie.setCardBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white_9_percent
                        )
                    )
                }
            }

            binding.cardMovie.setOnClickListener {
                onClick?.invoke(position, item)
            }
        }
    }
}
