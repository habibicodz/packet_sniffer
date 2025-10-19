package com.profusec.firewall.packetwall.services

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.VpnService
import android.os.Binder
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.profusec.firewall.packetwall.model.AppFilter
import com.profusec.firewall.packetwall.repositories.AppFilterRepository
import com.profusec.firewall.packetwall.state.NetworkTypeStatus
import com.profusec.firewall.packetwall.ui.status.CellularFilterStatus
import com.profusec.firewall.packetwall.ui.status.WifiFilterStatus
import com.profusec.firewall.packetwall.socket.PacketUtil
import com.profusec.firewall.packetwall.util.getInstalledApplications
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream

class FirewallService : VpnService() {
    private var applicationsToBlock: ArrayList<String> = arrayListOf()
    private lateinit var appFilterRepository: AppFilterRepository
    private lateinit var connectivityManager: ConnectivityManager
    private var networkTypeStatus: NetworkTypeStatus = NetworkTypeStatus.NOT_CONNECTED

    private var vpnInterface: ParcelFileDescriptor? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null


    override fun onCreate() {
        super.onCreate()
        appFilterRepository = AppFilterRepository(applicationContext)
        connectivityManager = applicationContext.getSystemService(ConnectivityManager::class.java)
        startForeground(
            FOREGROUND_SERVICE_ID,
            AppNotificationManager.createProxyServiceNotification(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            networkTypeStatus = getNetworkTypeStatus()
            initFilterApps()
            startProxyTunnel()
        }
        return START_STICKY
    }

    private suspend fun initFilterApps() = withContext(Dispatchers.IO) {
        val allApps = ArrayList(getInstalledApplications(applicationContext))
        val allAppsFilter =
            allApps.map { AppFilter(packageName = it) }.associateBy { it.packageName }
        val hashMap = HashMap(
            allAppsFilter.toMutableMap().also { mutableMap ->
                mutableMap.putAll(
                    appFilterRepository.getAppsFilters(allApps).associateBy { it.packageName })
            })

        val filterList = when (networkTypeStatus) {
            NetworkTypeStatus.CELLULAR -> {
                hashMap.filter { it.value.cellularFilterStatus == CellularFilterStatus.CELLULAR_NOT_ALLOWED }
                    .map { it.value.packageName }
            }

            NetworkTypeStatus.WIFI -> {
                hashMap.filter { it.value.wifiFilterStatus == WifiFilterStatus.WIFI_NOT_ALLOWED }
                    .map { it.value.packageName }
            }

            else -> {
                arrayListOf()
            }
        }

        applicationsToBlock.clear()
        applicationsToBlock.addAll(filterList)
    }

    private fun getNetworkTypeStatus(): NetworkTypeStatus {
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork == null) {
            return NetworkTypeStatus.NOT_CONNECTED
        }


        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkCapabilities == null) {
            return NetworkTypeStatus.NOT_CONNECTED
        }

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return NetworkTypeStatus.CELLULAR
        }

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return NetworkTypeStatus.WIFI
        }

        return NetworkTypeStatus.NOT_CONNECTED
    }

    fun startProxyTunnel() {
        if (vpnInterface != null) {
            return
        }

        val builder = Builder()
        val vpnInterfaceBuilder =
            builder.addAddress("192.168.2.2", 24).addRoute("0.0.0.0", 0).addDnsServer("192.168.1.1")

        applicationsToBlock.forEach {
            vpnInterfaceBuilder.addAllowedApplication(it)
        }

        vpnInterface = vpnInterfaceBuilder.establish()

        job = coroutineScope.launch {
            tunLoop(vpnInterface!!, this)
        }
    }

    suspend fun tunLoop(pfd: ParcelFileDescriptor, scope: CoroutineScope) {
        withContext(Dispatchers.IO) {
            val input = FileInputStream(pfd.fileDescriptor)
            val output = FileOutputStream(pfd.fileDescriptor)

            val buf = ByteArray(32768)

            while (scope.isActive) {
                try {
                    val read = input.read(buf)
                    if (read > 0) {
                        PacketUtil.parsePacket(buf)
                        output.write(buf, 0, read)
                        output.flush()
                    } else {
                        delay(5)
                    }

                } catch (e: CancellationException) {
                    e.printStackTrace()
                    break
                } catch (e: Throwable) {
                    e.printStackTrace()
                    delay(50)
                }
            }

            try {
                input.close()
                output.close()
            } catch (_: Throwable) {

            }
        }
    }

    override fun onDestroy() {
        stopTun()
        super.onDestroy()
    }

    fun stopProxyService() {
        stopTun()
        stopSelf()
    }

    private fun stopTun() {
        coroutineScope.coroutineContext.cancelChildren()
        job?.cancel()
        job = null
        try {
            vpnInterface?.close()
        } catch (_: Throwable) {
        }
        vpnInterface = null
    }

    private var binder: MyBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class MyBinder : Binder() {
        val firewallService: FirewallService get() = this@FirewallService
    }

    companion object {
        private const val FOREGROUND_SERVICE_ID = 0x0000FF
    }
}