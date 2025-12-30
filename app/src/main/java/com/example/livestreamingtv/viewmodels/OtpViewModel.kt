package com.example.livestreamingtv.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livestreamingtv.utils.OTPState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor() : ViewModel() {
    
    private val dummyuOTP = "1234"
    
    private val _otpState = MutableStateFlow<OTPState>(OTPState.Idle)
    val otpState: StateFlow<OTPState> = _otpState.asStateFlow()
    
    private val _timeSeconds = MutableStateFlow(60)
    val timeSecons: StateFlow<Int> = _timeSeconds.asStateFlow()
    
    private var timerJob: Job? = null
    
    init {
        startTime()
    }
    
    fun verifytOTP(otp: String) = viewModelScope.launch(Dispatchers.IO) {
        _otpState.value = OTPState.Loadiing
        if (otp == dummyuOTP) {
            _otpState.value = OTPState.Success
        } else {
            _otpState.value = OTPState.Error("Invalid OTP. Please try again.")
        }
    }
    
    fun resendOTP() = viewModelScope.launch(Dispatchers.IO) {
        _timeSeconds.value = 60
        _otpState.value = OTPState.OTPResent
    }
    
    private fun startTime() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            while (_timeSeconds.value > 0) {
                delay(1000)
                _timeSeconds.value -= 1
            }
        }
    }
    
    fun resetState() {
        _otpState.value = OTPState.Idle
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
