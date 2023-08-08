package com.anxops.bkn.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}

/**
 * Network utility to get current state of internet connection
 */
val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectionState {
    val network = connectivityManager.activeNetwork
    val connected = network?.let {
        connectivityManager.getNetworkCapabilities(it)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    } ?: false

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

/**
 * Network Utility to observe availability or unavailability of Internet connection
 */
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = networkStatusCallback {
        val state = getCurrentConnectivityState(connectivityManager)
        // Timber.d("[ConnectivityState] Network callback: ${state == ConnectionState.Available}")
        trySend(state)
    }

    val networkRequest = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

fun networkStatusCallback(callback: () -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback()
        }

        override fun onLost(network: Network) {
            callback()
        }
    }
}
