package com.profusec.firewall.packetwall.util

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getInstalledApplications(context: Context): List<String> = withContext(Dispatchers.IO) {
    val applicationContext = context.applicationContext
    val packageManager = applicationContext.packageManager
    packageManager.getInstalledApplications(PackageManager.GET_META_DATA).filter {
        packageManager.getLaunchIntentForPackage(it.packageName) != null
    }.map { it.packageName }
}