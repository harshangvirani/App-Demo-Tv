package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemGenreBinding

class SelectWhatYouSeeAdapter(
    private val genres: List<String>,
    private val onGenreClick: (String) -> Unit
) : RecyclerView.Adapter<SelectWhatYouSeeAdapter.GenreViewHolder>() {

    private val selectedGenres = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position], selectedGenres.contains(genres[position]), position)
    }

    override fun getItemCount(): Int = genres.size

    inner class GenreViewHolder(
        private val binding: ItemGenreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardGenre.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val genre = genres[position]
                    if (selectedGenres.contains(genre)) {
                        selectedGenres.remove(genre)
                    } else {
                        selectedGenres.add(genre)
                    }
                    notifyItemChanged(position)
                    onGenreClick(genre)
                }
            }
        }

        fun bind(genre: String, isSelected: Boolean, position: Int) {
            binding.tvGenreName.text = genre

            // Apply alternating margins based on ROW (not position) in 2-column grid
            // Row 0, 2, 4... (even rows) = padding at start
            // Row 1, 3, 5... (odd rows) = padding at end
            val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
            val rowNumber = position / 2  // Calculate which row this item is in

            if (rowNumber % 2 == 0) {
                // Even row - padding at start
                layoutParams.marginStart = 40
                layoutParams.marginEnd = 8
            } else {
                // Odd row - padding at end
                layoutParams.marginStart = 8
                layoutParams.marginEnd = 40
            }
            binding.root.layoutParams = layoutParams

            // Highlight selected items
            if (isSelected) {
                binding.cardGenre.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                binding.tvGenreName.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.black)
                )
            } else {
                val grayColor = ContextCompat.getColor(binding.root.context, R.color.white)
                val alphaColor = (0.1f * 255).toInt() shl 24 or (grayColor and 0x00FFFFFF)
                binding.cardGenre.setCardBackgroundColor(
                    alphaColor
                )
                binding.tvGenreName.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.white)
                )
            }

            // Focus effect
            binding.cardGenre.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.animate()
                        .scaleX(1.05f)
                        .scaleY(1.05f)
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
    }

    fun getSelectedGenres(): Set<String> = selectedGenres
}
