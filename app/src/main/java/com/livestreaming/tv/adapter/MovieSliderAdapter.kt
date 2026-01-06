package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.databinding.ItemMovieSliderBinding
import com.livestreaming.tv.utils.Movie

class MoviePagerAdapter() : RecyclerView.Adapter<MoviePagerAdapter.MovieViewHolder>() {

    private var selectedPosition = 3

    private val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val moviesList = AsyncListDiffer(this, diffUtil)
    var onBackgroundImageItem: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            ItemMovieSliderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.poster(moviesList.currentList[position], position == selectedPosition)
    }

    override fun getItemCount() = moviesList.currentList.size

    inner class MovieViewHolder(private val binding: ItemMovieSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun poster(movie: Movie, isCenter: Boolean) {
            with(binding) {
                ivPoster.setImageResource(movie.poster)
                onBackgroundImageItem?.invoke(movie)

                //Center item logic
                cardRoot.isFocusable = isCenter
                cardRoot.isFocusableInTouchMode = isCenter

                cardRoot.alpha = if (isCenter) 1f else 0.5f
                cardRoot.scaleX = if (isCenter) 1.1f else 0.9f
                cardRoot.scaleY = if (isCenter) 1.1f else 0.9f

                cardRoot.setOnFocusChangeListener { v, hasFocus ->
                    v.animate().scaleX(if (hasFocus) 1.15f else 1.1f)
                        .scaleY(if (hasFocus) 1.15f else 1.1f).setDuration(150).start()
                    if (hasFocus) {
                        cardRoot.strokeWidth = 2
                    } else {
                        cardRoot.strokeWidth = 0
                    }
                }
            }

        }
    }

    fun updateSelected(position: Int) {
        val old = selectedPosition
        selectedPosition = position
        notifyItemChanged(old)
        notifyItemChanged(position)
    }
}
