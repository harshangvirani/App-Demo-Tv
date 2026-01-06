package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.databinding.ItemAllLiveChannelsBinding
import com.livestreaming.tv.utils.AllLiveChannelItem

class AllLiveChannelAdapter(
    private val allLiveChannelItemDummy: List<AllLiveChannelItem>,
    private val onContentClick: (Int, AllLiveChannelItem) -> Unit,
) : RecyclerView.Adapter<AllLiveChannelAdapter.AllLiveChannelViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AllLiveChannelViewHolder = AllLiveChannelViewHolder(
        ItemAllLiveChannelsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: AllLiveChannelViewHolder,
        position: Int,
    ) {
        holder.bind(allLiveChannelItemDummy[position], position)
    }

    override fun getItemCount(): Int = allLiveChannelItemDummy.size

    inner class AllLiveChannelViewHolder(val binding: ItemAllLiveChannelsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContentClick(position, allLiveChannelItemDummy[position])
                }
            }

            // Add scale animation on focus
            binding.root.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                } else {
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                }
            }
        }

        fun bind(item: AllLiveChannelItem, position1: Int) {
            with(binding) {
                ivChannel.setImageResource(item.channelImg)
                tvChannelName.text = item.channelName
            }
        }

    }
}