package com.profusec.firewall.packetwall.persistent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.profusec.firewall.packetwall.model.AppFilter

@Dao
interface AppFilterDao {
    @Insert(onConflict = REPLACE)
    fun insert(appFilter: com.profusec.firewall.packetwall.model.AppFilter)

    @Query("SELECT * FROM app_filters WHERE package_name LIKE :packageName LIMIT 1")
    fun getAppFilter(packageName: String): com.profusec.firewall.packetwall.model.AppFilter?


    @Query("SELECT * FROM app_filters WHERE package_name IN (:packageNames)")
    fun getAppsFilters(packageNames: List<String>): List<com.profusec.firewall.packetwall.model.AppFilter>
}