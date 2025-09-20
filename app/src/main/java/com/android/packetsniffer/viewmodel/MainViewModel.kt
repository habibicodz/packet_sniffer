package com.android.packetsniffer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.packetsniffer.state.ProxyServiceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var connectingStarted: Long = System.currentTimeMillis()
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting = _isConnecting.asStateFlow()

    private val _proxyServiceState =
        MutableStateFlow<ProxyServiceState>(ProxyServiceState.Disconnected)

    val proxyServiceState = _proxyServiceState.asStateFlow()


    suspend fun waitConnectingTimeout() {
        if (isConnecting.value) {
            val now = System.currentTimeMillis()
            val passedConnectingTime = now - connectingStarted
            val remainingTimeToWait = 1000 - passedConnectingTime
            if (remainingTimeToWait > 0) {
                delay(remainingTimeToWait)
            }
        }
    }

    fun proxyServiceConnected() {
        viewModelScope.launch {
            waitConnectingTimeout()
            _proxyServiceState.emit(ProxyServiceState.Connected)
            _isConnecting.emit(false)
        }
    }

    fun proxyServiceDisconnected() {
        viewModelScope.launch {
            waitConnectingTimeout()
            _proxyServiceState.emit(ProxyServiceState.Disconnected)
            _isConnecting.emit(false)
        }
    }

    fun proxyServiceStateConnecting() {
        viewModelScope.launch {
            connectingStarted = System.currentTimeMillis()
            _isConnecting.emit(true)
        }
    }
}