package com.profusec.firewall.packetwall.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppFilterRepository(
    context: Context
) {
    private val applicationContext: Context = context.applicationContext
    private val appDatabase = _root_ide_package_.com.profusec.firewall.packetwall.persistent.AppDatabase.getInstance(applicationContext)
    private val appFilterDao = appDatabase.appFilterDao()


    suspend fun insert(app: com.profusec.firewall.packetwall.model.AppFilter) = withContext(Dispatchers.IO) {
        val appFilter = appFilterDao.getAppFilter(app.packageName)?.copy(
            wifiFilterStatus = app.wifiFilterStatus, cellularFilterStatus = app.cellularFilterStatus
        ) ?: app
        appFilterDao.insert(appFilter)
    }

    suspend fun getAppsFilters(apps: List<String>): List<com.profusec.firewall.packetwall.model.AppFilter> = withContext(Dispatchers.IO) {
        appFilterDao.getAppsFilters(apps)
    }
}