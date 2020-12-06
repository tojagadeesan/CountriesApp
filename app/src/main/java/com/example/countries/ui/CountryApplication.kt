package com.example.countries.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class CountryApplication : Application() {

    private val connectivityManager: ConnectivityManager
        get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun isNetworkAvailable(): Boolean {

        val connectivityManager = connectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } else {
            val wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting) {
                return true
            }

            val mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting) {
                return true
            }

            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

    companion object {
        public lateinit var INSTANCE: CountryApplication
    }
}