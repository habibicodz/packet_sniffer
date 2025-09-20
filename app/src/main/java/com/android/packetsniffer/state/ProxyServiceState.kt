package com.android.packetsniffer.state

sealed class ProxyServiceState {
    data object Disconnected: ProxyServiceState()
    data object Connected: ProxyServiceState()
}