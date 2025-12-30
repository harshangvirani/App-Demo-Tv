package com.example.livestreamingtv.ui.fragments.tvshows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.livestreamingtv.R
import com.example.livestreamingtv.databinding.FragmentTvShowsBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class TvShowsFragment : Fragment() {
    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTvShowsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
