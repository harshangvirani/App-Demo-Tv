package com.livestreaming.tv.ui.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.livestreaming.tv.databinding.FragmentLoginWithQrBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWithQrFragment : Fragment() {

    private var _binding: FragmentLoginWithQrBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginWithQrBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        setuiView()
    }

    private fun setuiView() {
        with(binding) {
            btnResend.setOnClickListener {

            }
            btnCancel.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }


}