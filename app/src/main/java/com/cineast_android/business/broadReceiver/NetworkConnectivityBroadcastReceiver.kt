package com.cineast_android.business.broadReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cineast_android.utils.NetUtils


class NetworkConnectivityBroadcastReceiver(private val connSink: ConnectivitySink) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            connSink.updateNetworkConnected(NetUtils.isOnline(context))
        }
    }
}