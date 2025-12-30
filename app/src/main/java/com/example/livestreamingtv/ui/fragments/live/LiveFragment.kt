package com.example.livestreamingtv.ui.fragments.live

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.livestreamingtv.R
import com.example.livestreamingtv.databinding.FragmentLiveBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class LiveFragment : Fragment() {
    private var _binding: FragmentLiveBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLiveBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
