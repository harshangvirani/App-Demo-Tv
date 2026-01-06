package com.livestreaming.tv.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.net.HttpURLConnection
import java.net.URL

class NetworkManager(private val connectivityManager: ConnectivityManager) {

    // LiveData to observe the network connection status
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    // Function to check if the device is connected to Wi-Fi
    private fun isWifiConnected(): Boolean {
        // Get the active network
        val activeNetwork: Network? = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }

    // Function to check Internet connection by pinging a URL (e.g., google.com)
    private fun isInternetAvailable(): Boolean {
        return try {
            val url = URL("https://www.google.com")
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 1500
            urlConnection.readTimeout = 1500
            urlConnection.connect()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Function to check both Wi-Fi and internet connection and update LiveData
    fun checkNetworkStatus() {
        _isConnected.value = if (isWifiConnected()) {
            isInternetAvailable() // Check if the internet is available
        } else {
            false
        }
    }
}

