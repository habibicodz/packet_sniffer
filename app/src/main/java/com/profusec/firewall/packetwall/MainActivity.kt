package com.profusec.firewall.packetwall

import android.Manifest
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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.profusec.firewall.packetwall.navigation.navhost.AppNavHost
import com.profusec.firewall.packetwall.services.FirewallService
import com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme
import com.profusec.firewall.packetwall.util.AppThemeMode
import com.profusec.firewall.packetwall.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        private const val VPN_PROFILE_REQUEST_CODE = 0xFF12FF
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 0xFF11FF
    }

    private lateinit var mViewModel: MainViewModel

    private var firewallService: FirewallService? = null
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?, service: IBinder?
        ) {
            val myBinder = service as FirewallService.MyBinder
            firewallService = myBinder.firewallService
            mViewModel.proxyServiceConnected()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            firewallService = null
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
            LocalContext.current
            val appTheme by mViewModel.appTheme.collectAsStateWithLifecycle()
            val isSystemDark = isSystemInDarkTheme()
            val isDarkTheme by remember(appTheme) {
                mutableStateOf(
                    when (appTheme) {
                        AppThemeMode.SYSTEM -> {
                            isSystemDark
                        }

                        AppThemeMode.DARK -> {
                            true
                        }

                        else -> {
                            false
                        }
                    }
                )
            }

            val navController = rememberNavController()

            PacketSnifferTheme(
                darkTheme = isDarkTheme
            ) {
                AppNavHost(
                    mViewModel = mViewModel, navController = navController, onToggleClicked = {
                        toggleProxyService()
                    })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(applicationContext, FirewallService::class.java), serviceConnection, 0
        )
    }

    override fun onStop() {
        super.onStop()
        firewallService?.let {
            unbindService(serviceConnection)
        }
    }

    private fun toggleProxyService() {
        if (mViewModel.isConnecting.value || !checkVpnProfileGranted()) {
            return
        }

        if (firewallService == null) {
            startProxyService()
        } else {
            stopProxyService()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    application, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
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
        requestCode: Int, resultCode: Int, data: Intent?, caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (requestCode == VPN_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            startProxyService()
        }
    }

    private fun startProxyService() {
        lifecycleScope.launch {
            val firewallServiceIntent = Intent(applicationContext, FirewallService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(
                    firewallServiceIntent
                )
            } else {
                startService(
                    firewallServiceIntent
                )
            }

            mViewModel.proxyServiceStateConnecting()
            bindService(firewallServiceIntent, serviceConnection, 0)
        }
    }

    private fun stopProxyService() {
        lifecycleScope.launch {
            mViewModel.proxyServiceStateConnecting()
            firewallService?.stopProxyService()
        }
    }
}