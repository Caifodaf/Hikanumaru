package ru.android.hikanumaruapp.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Build.*
import android.os.Build.VERSION.SDK_INT

object NetworkUtils {
    @SuppressLint("ObsoleteSdkInt")
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (SDK_INT >= VERSION_CODES.M) {
            // Для устройств с Android 6+ используем NetworkCapabilities
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            // Для устройств с Android ниже 6 используем deprecated метод
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}