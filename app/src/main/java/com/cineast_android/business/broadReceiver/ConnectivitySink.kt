package com.cineast_android.business.broadReceiver

interface ConnectivitySink {
    fun updateNetworkConnected(isConnected: Boolean)
}