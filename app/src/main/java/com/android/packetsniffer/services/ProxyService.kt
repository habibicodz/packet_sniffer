package com.android.packetsniffer.services

import android.content.Intent
import android.net.VpnService
import android.os.Binder
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

class ProxyService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null
    private var job: Job? = null


    override fun onCreate() {
        super.onCreate()
        startForeground(
            FOREGROUND_SERVICE_ID,
            AppNotificationManager.createProxyServiceNotification(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startProxyTunnel()
        return START_STICKY
    }

    fun startProxyTunnel() {
        if (vpnInterface != null) {
            return
        }

        val builder = Builder()
        vpnInterface = builder
            .addAddress("192.168.2.2", 24)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("192.168.1.1")
            .establish()

        job = CoroutineScope(Dispatchers.IO).launch {
            tunLoop(vpnInterface!!, this)
        }
    }


    suspend fun tunLoop(pfd: ParcelFileDescriptor, scope: CoroutineScope) {
        val input = FileInputStream(pfd.fileDescriptor)
        val output = FileOutputStream(pfd.fileDescriptor)

        val buf = ByteArray(32768)

        while (scope.isActive) {
            try {
                val read = input.read(buf)
                if (read > 0) {
                    Log.d("analyze", "Read $read bytes from TUN")

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
            Log.d("analyze", "Closing streams")
            Log.d("analyze", "Closed streams")
            input.close()
            output.close()
        } catch (_: Throwable) {

        }
    }

    override fun onDestroy() {
        Log.d("analyze", "On destroy called")
        stopTun()
        super.onDestroy()
    }

    fun stopProxyService () {
        stopTun()
        stopSelf()
    }

    private fun stopTun() {
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
        val proxyService: ProxyService get() = this@ProxyService
    }

    companion object {
        private const val FOREGROUND_SERVICE_ID = 0x0000FF
    }
}