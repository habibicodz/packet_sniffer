package com.profusec.firewall.packetwall.model

import android.graphics.drawable.Drawable

data class MiniTrackedAppItem(
    val packageName: String,
    val appIcon: Drawable,
    val appName: String? = null,
)