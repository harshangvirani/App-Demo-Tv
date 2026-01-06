package com.livestreaming.tv.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.databinding.ItemMovieHomeListBinding
import com.livestreaming.tv.utils.ImagesItem

class MoviesRelatedAdapter() : RecyclerView.Adapter<MoviesRelatedAdapter.MoviesViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ImagesItem>() {
        override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val imageList = AsyncListDiffer(this, diffUtil)
    var onClick: ((ImagesItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MoviesViewHolder = MoviesViewHolder(
        ItemMovieHomeListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(
        holder: MoviesViewHolder,
        position: Int,
    ) {
        holder.bind(imageList.currentList[position], position)
    }

    override fun getItemCount(): Int = imageList.currentList.size


    inner class MoviesViewHolder(private val binding: ItemMovieHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.spImage.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    binding.spImage.apply {
                        strokeWidth = 2f
                        strokeColor = ColorStateList.valueOf(Color.WHITE)
                    }
                } else {
                    binding.spImage.apply {
                        strokeWidth = 0f
                        strokeColor = ColorStateList.valueOf(Color.WHITE)
                    }
                }
            }
        }

        fun bind(imagesItem: ImagesItem, position: Int) {
            binding.spImage.apply {
                setImageResource(imagesItem.img)
                setOnClickListener {
                    onClick?.invoke(imagesItem)
                }
            }
        }
    }
}