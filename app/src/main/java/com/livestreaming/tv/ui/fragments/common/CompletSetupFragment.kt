package com.livestreaming.tv.ui.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.FragmentCompletSetupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletSetupFragment : Fragment() {

    private var _binding: FragmentCompletSetupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCompletSetupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnVerify.setOnClickListener {
                findNavController().navigate(R.id.forYouFragment)
            }
        }
    }
}