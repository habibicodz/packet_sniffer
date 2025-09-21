package com.android.packetsniffer.viewmodel

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.packetsniffer.state.ProxyServiceState
import com.android.packetsniffer.util.getInstalledApplications
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val applicationContext: Context = application.applicationContext

    private val _allInstalledApplications = MutableStateFlow<ArrayList<String>?>(null)
    val allInstalledApplications = _allInstalledApplications.asStateFlow()

    private var connectingStarted: Long = System.currentTimeMillis()
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting = _isConnecting.asStateFlow()

    private val _proxyServiceState =
        MutableStateFlow<ProxyServiceState>(ProxyServiceState.Disconnected)

    val proxyServiceState = _proxyServiceState.asStateFlow()

    init {
        initAllApplications()
    }

    private fun initAllApplications() {
        viewModelScope.launch {
            _allInstalledApplications.emit(ArrayList(getInstalledApplications(applicationContext)))
        }
    }


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