package com.example.livestreamingtv.ui.fragments.subscriptions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.livestreamingtv.R
import com.example.livestreamingtv.databinding.FragmentSubscriptionBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SubscriptionFragment : Fragment() {
    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSubscriptionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
