package com.example.livestreamingtv.ui.fragments.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.livestreamingtv.R
import com.example.livestreamingtv.databinding.FragmentLoginWihtPhoneScreenBinding
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
            etMobile.requestFocus()
            
            // Handle D-pad on Mobile Input
            etMobile.setOnKeyListener { _, keyCode, event ->
                if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_DOWN -> {
                            btnSend.requestFocus()
                            true
                        }
                        android.view.KeyEvent.KEYCODE_DPAD_UP -> {
                            btnBack.requestFocus()
                            true
                        }
                        android.view.KeyEvent.KEYCODE_ENTER, 
                        android.view.KeyEvent.KEYCODE_DPAD_CENTER -> {
                            // Hide keyboard and move to send button or click it?
                            // For now, let's just click send if we want easy submission
                            btnSend.performClick()
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }

            // Handle D-pad on Send Button
            btnSend.setOnKeyListener { _, keyCode, event ->
                if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_UP -> {
                            etMobile.requestFocus()
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }

            // Handle D-pad on Back Button
            btnBack.setOnKeyListener { _, keyCode, event ->
                if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_DOWN -> {
                            etMobile.requestFocus()
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }

            btnSend.setOnClickListener {
                Log.d(requireContext().toString(), "Click")
                findNavController().navigate(R.id.action_loginWihtPhoneScreenFragment_to_loginOtpScreenFragment)
            }
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
