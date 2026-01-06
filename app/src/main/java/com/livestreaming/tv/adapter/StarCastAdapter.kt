package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemStarCastBinding
import com.livestreaming.tv.utils.Img
import kotlin.math.min

class StarCastAdapter() : RecyclerView.Adapter<StarCastAdapter.StarCastViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Img>() {
        override fun areItemsTheSame(oldItem: Img, newItem: Img): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Img, newItem: Img): Boolean {
            return oldItem == newItem // compares all properties
        }
    }

    val starCastList = AsyncListDiffer(this, diffUtil)
    var onClick: ((Img) -> Unit)? = null
    var isExpanded = false


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StarCastViewHolder = StarCastViewHolder(
        ItemStarCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: StarCastViewHolder,
        position: Int,
    ) {
        holder.bind(starCastList.currentList[position], position)
    }

    override fun getItemCount(): Int {
        return if (isExpanded) {
            starCastList.currentList.size
        } else {
            minOf(starCastList.currentList.size, 10)
        }
    }

    inner class StarCastViewHolder(private val binding: ItemStarCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(starCastModel: Img?, position: Int) {
            with(binding) {
                ivProfile.setImageResource(starCastModel?.img ?: R.drawable.ct_1)
                tvName.text = starCastModel?.string
            }
        }
    }

    fun toggleExpand() {
        isExpanded = !isExpanded
        notifyDataSetChanged()
    }
}