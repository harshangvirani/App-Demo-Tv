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

class MovieShowImageAdapter() :
    RecyclerView.Adapter<MovieShowImageAdapter.MovieShowImageViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ImagesItem>() {
        override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val imgList = AsyncListDiffer(this, diffUtil)
    var onClick: ((ImagesItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieShowImageViewHolder = MovieShowImageViewHolder(
        ItemTvShowListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(
        holder: MovieShowImageViewHolder,
        position: Int,
    ) {
        holder.bind(imgList.currentList[position])
    }

    override fun getItemCount(): Int = imgList.currentList.size

    inner class MovieShowImageViewHolder(private val bindng: ItemTvShowListBinding) :
        RecyclerView.ViewHolder(bindng.root) {
        init {
            bindng.cardMovie.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                } else {
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                }
            }
        }

        fun bind(imgItem: ImagesItem) {
            with(bindng) {
                imgPoster.setImageResource(imgItem.img)

                //Focus Listen RecyclerViewItem
                cardMovie.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        cardMovie.setCardBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.white
                            )
                        )
                    } else {
                        cardMovie.setCardBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.white_9_percent
                            )
                        )
                    }
                }

                cardMovie.setOnClickListener {
                    onClick?.invoke(imgItem)
                }
            }
        }
    }
}