package com.profusec.firewall.packetwall.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.profusec.firewall.packetwall.ui.status.CellularFilterStatus
import com.profusec.firewall.packetwall.ui.status.WifiFilterStatus

@Entity(tableName = "app_filters")
data class AppFilter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "wifi_filter_status") val wifiFilterStatus: WifiFilterStatus = WifiFilterStatus.WIFI_ALLOWED,
    @ColumnInfo(name = "cellular_filter_status") val cellularFilterStatus: CellularFilterStatus = CellularFilterStatus.CELLULAR_ALLOWED
)