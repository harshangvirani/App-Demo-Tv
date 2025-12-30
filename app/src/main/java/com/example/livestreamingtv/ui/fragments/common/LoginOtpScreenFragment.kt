package com.example.livestreamingtv.ui.fragments.common

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.livestreamingtv.R
import com.example.livestreamingtv.databinding.FragmentLoginOtpScreenBinding
import com.example.livestreamingtv.utils.OTPState
import com.example.livestreamingtv.viewmodels.OtpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginOtpScreenFragment : Fragment() {
    
    private var _binding: FragmentLoginOtpScreenBinding? = null
    private val binding get() = _binding!!
    private val otpViewModel: OtpViewModel by viewModels()
    private lateinit var otpBoxes: List<EditText>
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginOtpScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOTPBox()
        setupView()
        setObserver()
    }
    
    private fun setupOTPBox() {
        with(binding) {
            otpBoxes = listOf(
                etOtp1,
                etOtp2,
                etOtp3,
                etOtp4
            )
        }
        
        otpBoxes.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                private var oldText = ""
                
                override fun afterTextChanged(s: Editable?) {
                    binding.tvError.visibility = View.GONE
                }
                
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                    oldText = s.toString()
                }
                
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (s?.length == 1) {
                        if (index < otpBoxes.size - 1) {
                            otpBoxes[index + 1].requestFocus()
                        } else {
                            editText.clearFocus()
                            // Optional: Move focus to verify button when last digit entered?
                            // binding.btnVerify.requestFocus() 
                        }
                    } else if (s?.length ?: 0 > 1) {
                        val newDigt = s?.last()?.toString() ?: ""
                        editText.setText(newDigt)
                        editText.setSelection(newDigt.length)
                    }
                }
            })
            
            editText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            if (index < otpBoxes.size - 1) {
                                otpBoxes[index + 1].requestFocus()
                                return@setOnKeyListener true
                            }
                        }
                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            if (index > 0) {
                                otpBoxes[index - 1].requestFocus()
                                return@setOnKeyListener true
                            }
                        }
                        KeyEvent.KEYCODE_DPAD_DOWN -> {
                            binding.btnVerify.requestFocus()
                            return@setOnKeyListener true
                        }
                        KeyEvent.KEYCODE_DEL -> {
                            if (editText.text.isEmpty() && index > 0) {
                                otpBoxes[index - 1].requestFocus()
                                otpBoxes[index - 1].text.clear()
                                return@setOnKeyListener true
                            }
                        }
                    }
                }
                false
            }
        }
        
        binding.etOtp1.requestFocus()
        
        // Button Navigation
        binding.btnVerify.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        binding.etOtp1.requestFocus()
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        if (binding.tvResendOtp.isFocusable) {
                             binding.tvResendOtp.requestFocus()
                             true
                        } else {
                            false
                        }
                    }
                    else -> false
                }
            } else {
                false
            }
        }

        binding.tvResendOtp.setOnKeyListener { _, keyCode, event ->
             if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        binding.btnVerify.requestFocus()
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    }
    
    private fun setupView() {
        with(binding) {
            btnVerify.setOnClickListener {
                val otp = getOTP()
                if (otp.length == 4) {
                    otpViewModel.verifytOTP(otp)
                } else {
                    showingError("Please enter complete OTP")
                }
            }
            
            tvResendOtp.setOnClickListener {
                otpViewModel.resendOTP()
            }
        }
    }
    
    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            otpViewModel.otpState.collect {
                handleCollection(it)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            otpViewModel.timeSecons.collect {
                with(binding) {
                    if (it > 0) {
                        tvResendOtp.apply {
                            text = "${getString(R.string.resend)} - ${it}"
                            isClickable = false
                            isFocusable = false
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_gray
                                )
                            )
                        }
                    } else {
                        tvResendOtp.apply {
                            text = getString(R.string.resend)
                            isClickable = true
                            isFocusable = true
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }
                }
            }
        }
    }
    
    private fun handleCollection(state: OTPState) {
        with(binding) {
            when (state) {
                is OTPState.Idle -> {
                    btnVerify.isEnabled = true
                    btnVerify.text = getString(R.string.verify_otp)
                }
                is OTPState.Loadiing -> {
                    btnVerify.isEnabled = false
                    btnVerify.text = getString(R.string.verifying)
                    tvError.visibility = GONE
                }
                is OTPState.Success -> {
                    findNavController().navigate(R.id.action_loginOtpScreenFragment_to_userSelectionFragment)
                }
                is OTPState.Error -> {
                    showingError(state.message)
                    setOTPBoxErrorColor()
                    binding.etOtp1.requestFocus()
                }
                is OTPState.OTPResent -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.otp_has_been_resent),
                        Toast.LENGTH_SHORT
                    ).show()
                    otpViewModel.resetState()
                    resetOTPBoxColor()
                    clearOTP()
                }
            }
        }
    }
    
    private fun getOTP(): String = otpBoxes.joinToString("") { it.text.toString() }
    
    private fun clearOTP() {
        otpBoxes.forEach {
            it.text.clear()
        }
    }
    
    private fun showingError(message: String) {
        binding.tvError.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    
    private fun setOTPBoxErrorColor() {
        otpBoxes.forEach {
            it.setBackgroundResource(R.drawable.otp_box_error_background)
        }
    }
    
    private fun resetOTPBoxColor() {
        otpBoxes.forEach {
            it.setBackgroundResource(R.drawable.otp_box_background)
        }
    }
}
