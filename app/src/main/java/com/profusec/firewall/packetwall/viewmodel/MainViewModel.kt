package com.profusec.firewall.packetwall.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.profusec.firewall.packetwall.repositories.AppFilterRepository
import com.profusec.firewall.packetwall.state.FirewallServiceState
import com.profusec.firewall.packetwall.util.AppThemeMode
import com.profusec.firewall.packetwall.util.ThemeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val progressDelayMillis = 500

    private val applicationContext: Context = application.applicationContext
    private val appFilterRepository: AppFilterRepository = AppFilterRepository(applicationContext)


    private val _appsFilters = MutableStateFlow<HashMap<String, com.profusec.firewall.packetwall.model.AppFilter>>(hashMapOf())
    val appsFilters = _appsFilters.asStateFlow()

    private var connectingStarted: Long = System.currentTimeMillis()

    private val _appTheme = MutableStateFlow(ThemeUtil.getAppTheme(applicationContext))
    val appTheme = _appTheme.asStateFlow()

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting = _isConnecting.asStateFlow()

    private val _firewallServiceState =
        MutableStateFlow<FirewallServiceState>(FirewallServiceState.Disconnected)
    val proxyServiceState = _firewallServiceState.asStateFlow()


    init {
        initFilterApps()
    }

    private fun initFilterApps() {
        viewModelScope.launch {
            val allApps = ArrayList(
                _root_ide_package_.com.profusec.firewall.packetwall.util.getInstalledApplications(
                    applicationContext
                )
            )
            val allAppsFilter =
                allApps.map {
                    _root_ide_package_.com.profusec.firewall.packetwall.model.AppFilter(
                        packageName = it
                    )
                }.associateBy { it.packageName }

            _appsFilters.emit(
                HashMap(
                    allAppsFilter.toMutableMap().also { mutableMap ->
                        mutableMap.putAll(
                            appFilterRepository.getAppsFilters(allApps)
                                .associateBy { it.packageName })
                    })
            )
        }
    }

    fun toggleWifiStatus(packageName: String) {
        viewModelScope.launch {
            _appsFilters.update {
                HashMap(it).also { mutableMap ->
                    val appFilter = mutableMap[packageName]!!
                    val updatedAppFilter = appFilter.copy(
                        wifiFilterStatus = if (appFilter.wifiFilterStatus == com.profusec.firewall.packetwall.ui.status.WifiFilterStatus.WIFI_ALLOWED) {
                            com.profusec.firewall.packetwall.ui.status.WifiFilterStatus.WIFI_NOT_ALLOWED
                        } else {
                            com.profusec.firewall.packetwall.ui.status.WifiFilterStatus.WIFI_ALLOWED
                        }
                    )
                    mutableMap[packageName] = updatedAppFilter

                    appFilterRepository.insert(
                        updatedAppFilter
                    )
                }
            }
        }
    }

    fun toggleCellularStatus(
        packageName: String
    ) {
        viewModelScope.launch {
            _appsFilters.update {
                HashMap(it).also { mutableMap ->
                    val appFilter = mutableMap[packageName]!!
                    val updatedAppFilter = appFilter.copy(
                        cellularFilterStatus = if (appFilter.cellularFilterStatus == com.profusec.firewall.packetwall.ui.status.CellularFilterStatus.CELLULAR_ALLOWED) {
                            com.profusec.firewall.packetwall.ui.status.CellularFilterStatus.CELLULAR_NOT_ALLOWED
                        } else {
                            com.profusec.firewall.packetwall.ui.status.CellularFilterStatus.CELLULAR_ALLOWED
                        }
                    )
                    mutableMap[packageName] = updatedAppFilter

                    appFilterRepository.insert(
                        updatedAppFilter
                    )
                }
            }
        }
    }


    suspend fun waitConnectingTimeout() {
        if (isConnecting.value) {
            val now = System.currentTimeMillis()
            val passedConnectingTime = now - connectingStarted
            val remainingTimeToWait = progressDelayMillis - passedConnectingTime
            if (remainingTimeToWait > 0) {
                delay(remainingTimeToWait)
            }
        }
    }

    fun proxyServiceConnected() {
        viewModelScope.launch {
            waitConnectingTimeout()
            _firewallServiceState.emit(FirewallServiceState.Connected)
            _isConnecting.emit(false)
        }
    }

    fun proxyServiceDisconnected() {
        viewModelScope.launch {
            waitConnectingTimeout()
            _firewallServiceState.emit(FirewallServiceState.Disconnected)
            _isConnecting.emit(false)
        }
    }

    fun proxyServiceStateConnecting() {
        viewModelScope.launch {
            connectingStarted = System.currentTimeMillis()
            _isConnecting.emit(true)
        }
    }


    fun setTheme(mode: AppThemeMode) {
        if (mode == _appTheme.value) {
            return
        }

        viewModelScope.launch {
            ThemeUtil.setThemeMode(
                applicationContext, mode
            )

            _appTheme.emit(
                mode
            )
        }
    }
}