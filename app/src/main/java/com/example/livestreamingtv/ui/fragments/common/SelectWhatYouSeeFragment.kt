package com.example.livestreamingtv.ui.fragments.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.livestreamingtv.R
import com.example.livestreamingtv.adapter.SelectWhatYouSeeAdapter
import com.example.livestreamingtv.databinding.FragmentSelectWhatYouSeeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectWhatYouSeeFragment : Fragment() {
    
    private var _binding: FragmentSelectWhatYouSeeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SelectWhatYouSeeAdapter
    
    private val genres = listOf(
        "Action",
        "Comedy",
        "Romance",
        "Drama",
        "Horror",
        "Thriller",
        "Sci-Fi",
        "Fantasy",
        "Documentary",
        "Animation",
        "Adventure",
        "Mystery"
    )
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       _binding = FragmentSelectWhatYouSeeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupNextButton()
    }
    
    private fun setupRecyclerView() {
        adapter = SelectWhatYouSeeAdapter(genres) { genre ->
            // Handle genre click
            Toast.makeText(requireContext(), "Toggled: $genre", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvGenres.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = this@SelectWhatYouSeeFragment.adapter
            // Auto-focus first item
            post {
                getChildAt(0)?.requestFocus()
            }
        }
    }
    
    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            val selectedGenres = adapter.getSelectedGenres()
            if (selectedGenres.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select at least one genre",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(R.id.action_selectWhatYouSeeFragment_to_loginWthSelectionFragment)
            }
        }
    }
}
