package com.livestreaming.tv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.ItemUserBinding
import com.livestreaming.tv.ui.fragments.common.userselection.UserProfile

class UserSelectionAdapter(
    private val profiles: List<UserProfile>,
    private val onProfileClick: (UserProfile, Int) -> Unit,
    private val onEditClick: (UserProfile) -> Unit,
) : RecyclerView.Adapter<UserSelectionAdapter.ProfileViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(profiles[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = profiles.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }

    inner class ProfileViewHolder(
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardProfile.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedPosition(position)
                    onProfileClick(profiles[position], position)
                }
            }

            binding.ivEditIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION && !profiles[position].isAddNew) {
                    onEditClick(profiles[position])
                }
            }
        }

        fun bind(profile: UserProfile, isSelected: Boolean) {
            if (profile.isAddNew) {
                // Show as "Add New" profile
                binding.ivProfile.visibility = View.GONE
                binding.ivAddIcon.visibility = View.VISIBLE
                binding.tvUsername.text = "Add Profile"
                binding.ivEditIcon.visibility = View.GONE
            } else {
                // Show as regular profile
                binding.ivProfile.visibility = View.VISIBLE
                binding.ivAddIcon.visibility = View.GONE
                binding.tvUsername.text = profile.name
                binding.ivEditIcon.visibility = View.VISIBLE
            }

            // Visual highlight for selected profile (using elevation only)
            if (isSelected) {
                binding.cardProfile.strokeWidth = 6
                binding.cardProfile.strokeColor = androidx.core.content.ContextCompat.getColor(
                    binding.root.context,
                    R.color.white
                )
                binding.cardProfile.cardElevation = 20f
            } else {
                binding.cardProfile.strokeWidth = 0
                binding.cardProfile.cardElevation = 8f
            }
            binding.cardProfile.isFocusable = true
            binding.cardProfile.isFocusableInTouchMode = true

            // Add focus listener for scale effect
            binding.cardProfile.setOnFocusChangeListener { view, hasFocus ->
                val card = view as com.google.android.material.card.MaterialCardView
                if (hasFocus) {
                    card.strokeWidth = 6
                    card.strokeColor = androidx.core.content.ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                    view.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(200)
                        .start()
                } else {
                    if (!isSelected) {
                        card.strokeWidth = 0
                    }
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(200)
                        .start()
                }
            }
        }
    }
}