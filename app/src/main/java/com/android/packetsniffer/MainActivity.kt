package com.android.packetsniffer

import android.app.ComponentCaller
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.android.packetsniffer.navigation.navhost.AppNavHost
import com.android.packetsniffer.services.ProxyService
import com.android.packetsniffer.ui.theme.PacketSnifferTheme
import com.android.packetsniffer.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        private const val VPN_PROFILE_REQUEST_CODE = 0xFF12FF
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 0xFF11FF
    }

    private lateinit var mViewModel: MainViewModel

    private var proxyService: ProxyService? = null
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val myBinder = service as ProxyService.MyBinder
            proxyService = myBinder.proxyService
            mViewModel.proxyServiceConnected()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            proxyService = null
            mViewModel.proxyServiceDisconnected()
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        checkNotificationPermission()
        setContent {
            val navController = rememberNavController()


            PacketSnifferTheme {
                AppNavHost(
                    mViewModel = mViewModel,
                    navController = navController,
                    onToggleClicked = {
                        toggleProxyService()
                    })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(applicationContext, ProxyService::class.java),
            serviceConnection,
            0
        )
    }

    override fun onStop() {
        super.onStop()
        proxyService?.let {
            unbindService(serviceConnection)
        }
    }

    private fun toggleProxyService() {
        if (mViewModel.isConnecting.value || !checkVpnProfileGranted()) {
            return
        }

        if (proxyService == null) {
            startProxyService()
        } else {
            stopProxyService()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    application,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun checkVpnProfileGranted(): Boolean {
        val profileIntent = VpnService.prepare(applicationContext)
        if (profileIntent != null) {
            startActivityForResult(profileIntent, VPN_PROFILE_REQUEST_CODE)
            return false
        }

        return true
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (requestCode == VPN_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            startProxyService()
        }
    }

    private fun startProxyService() {
        lifecycleScope.launch {
            val proxyServiceIntent = Intent(applicationContext, ProxyService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(
                    proxyServiceIntent
                )
            } else {
                startService(
                    proxyServiceIntent
                )
            }

            mViewModel.proxyServiceStateConnecting()
            bindService(proxyServiceIntent, serviceConnection, 0)
        }
    }

    private fun stopProxyService() {
        lifecycleScope.launch {
            mViewModel.proxyServiceStateConnecting()
            proxyService?.stopProxyService()
        }
    }
}