package com.profusec.firewall.packetwall.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    val applicationContext: Context = application.applicationContext
}