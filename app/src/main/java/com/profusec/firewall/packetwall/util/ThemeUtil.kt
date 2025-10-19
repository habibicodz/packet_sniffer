package com.profusec.firewall.packetwall.util

import android.content.Context
import androidx.core.content.edit
import com.profusec.firewall.packetwall.R

object ThemeUtil {
    fun getAppTheme(context: Context): AppThemeMode {
        val applicationContext: Context = context.applicationContext
        return AppThemeMode.valueOf(
            PreferenceUtil.getSharedPreferences(applicationContext)
                .getString(
                    applicationContext.getString(R.string.theme_mode_pref),
                    AppThemeMode.SYSTEM.name
                )!!
        )
    }

    fun setThemeMode(context: Context, mode: AppThemeMode) {
        val applicationContext = context.applicationContext
        PreferenceUtil.getSharedPreferences(
            applicationContext
        ).edit {
            putString(
                applicationContext.getString(R.string.theme_mode_pref),
                mode.name
            )
        }
    }
}

enum class AppThemeMode {
    DARK,
    LIGHT,
    SYSTEM
}