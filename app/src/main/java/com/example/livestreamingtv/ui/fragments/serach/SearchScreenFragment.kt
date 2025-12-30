package com.example.livestreamingtv.ui.fragments.serach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.livestreamingtv.databinding.FragmentSearchScreenBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SearchScreenFragment : Fragment() {
    private var _binding: FragmentSearchScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
