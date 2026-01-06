package com.livestreaming.tv.ui.fragments.serach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.livestreaming.tv.adapter.SearchCategoryAdapter
import com.livestreaming.tv.databinding.FragmentSearchScreenBinding
import com.livestreaming.tv.utils.categoryList
import com.livestreaming.tv.utils.hideKeyboard
import com.livestreaming.tv.utils.showKeyboard
import com.livestreaming.tv.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchScreenFragment : Fragment() {

    private var _binding: FragmentSearchScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding) {
            etSearch.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.showKeyboard()
                } else {
                    v.hideKeyboard()
                }
            }
            rvSearch.apply {
                layoutManager = GridLayoutManager(requireContext(), 5)
                adapter = SearchCategoryAdapter(categoryList) {
                    toast(it.toString())

                }
            }
        }
    }
}