package com.example.easymechanic.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    
    fun getBaseUrl(): String {
        // For Android Emulator: 10.0.2.2 maps to localhost
        // For Physical Device: Replace with your computer's IP address
        return "http://10.0.2.2/easymechanic/api/"
        
        // Uncomment and set your computer's IP for physical device testing:
        // return "http://192.168.1.XXX/easymechanic/api/"
    }
}

