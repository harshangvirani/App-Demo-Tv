package com.example.livestreamingtv.ui.fragments.common.userselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamingtv.R
import com.example.livestreamingtv.adapter.UserSelectionAdapter
import com.example.livestreamingtv.databinding.FragmentUserSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSelectionFragment : Fragment() {
    
    private var _binding: FragmentUserSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserSelectionAdapter
    private lateinit var layoutManager: CenterHorizontalLayoutManager
    
    private val profiles = mutableListOf(
        UserProfile("1", "John Doe"),
        UserProfile("2", "Jane Smith"),
        UserProfile("3", "Kids"),
        UserProfile("add", "Add Profile", isAddNew = true)
    )
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUserSelectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        layoutManager = CenterHorizontalLayoutManager(requireContext())
        adapter = UserSelectionAdapter(
            profiles,
            onProfileClick = { profile, position ->
                onProfileSelected(profile, position)
            },
            onEditClick = { profile ->
                onEditProfile(profile)
            }
        )
        
        binding.rvProfiles.apply {
            this.layoutManager = this@UserSelectionFragment.layoutManager
            this.adapter = this@UserSelectionFragment.adapter
            
            // Calculate padding to center the first/last item
            // Item width approx 150dp + 32dp margin = 182dp
            // Let's rely on measurement or generic calculation
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (182 * displayMetrics.density).toInt() // 150dp card + 32dp margin
            val padding = (screenWidth - itemWidth) / 2
            
            setPadding(padding, 0, padding, 0)
            clipToPadding = false
            
            // Add snap helper to snap to center
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
            
            // Scroll to first profile and set focus
            post {
                scrollToPosition(0)
                updateSelectedProfile(0)
                // Request focus on first item after layout
                postDelayed({
                    val firstChild = getChildAt(0)
                    firstChild?.requestFocus()
                }, 100)
            }
            
            // Listen to scroll to update selected item
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView =
                            snapHelper.findSnapView(this@UserSelectionFragment.layoutManager)
                        if (centerView != null) {
                            val position = recyclerView.getChildAdapterPosition(centerView)
                            if (position != RecyclerView.NO_POSITION) {
                                updateSelectedProfile(position)
                            }
                        }
                    }
                }
            })
        }
    }
    
    private fun updateSelectedProfile(position: Int) {
        if (position >= 0 && position < profiles.size) {
            adapter.setSelectedPosition(position)
        }
    }
    
    private fun onProfileSelected(profile: UserProfile, position: Int) {
        if (profile.isAddNew) {
            // Handle add new profile
            Toast.makeText(requireContext(), "Add new profile", Toast.LENGTH_SHORT).show()
        } else {
            // Navigate to home with selected profile
            findNavController().navigate(R.id.action_userSelectionFragment_to_completSetupFragment)
        }
    }
    
    private fun onEditProfile(profile: UserProfile) {
        Toast.makeText(requireContext(), "Edit profile: ${profile.name}", Toast.LENGTH_SHORT).show()
    }
}
