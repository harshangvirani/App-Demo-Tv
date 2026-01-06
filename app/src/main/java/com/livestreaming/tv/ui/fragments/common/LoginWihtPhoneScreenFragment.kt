package com.livestreaming.tv.ui.fragments.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.livestreaming.tv.databinding.FragmentLoginWihtPhoneScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWihtPhoneScreenFragment : Fragment() {

    private var _binding: FragmentLoginWihtPhoneScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginWihtPhoneScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        with(binding) {
            // etMobile.requestFocus()
            btnSend.setOnClickListener {
                Log.d(requireContext().toString(), "Click")
                findNavController().navigate(LoginWihtPhoneScreenFragmentDirections.actionLoginWihtPhoneScreenFragmentToLoginOtpScreenFragment())
            }
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

        }

        // Handle Enter key on username field
    }
}