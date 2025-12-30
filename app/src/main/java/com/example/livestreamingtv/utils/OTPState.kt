package com.example.livestreamingtv.utils

sealed class OTPState {
    object Idle : OTPState()
    object Loadiing : OTPState()
    object Success : OTPState()
    object OTPResent : OTPState()
    data class Error(val message: String) : OTPState()
}
