package com.profusec.firewall.packetwall.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.profusec.firewall.packetwall.R

object PreferenceUtil {

    fun isPinSet(context: Context): Boolean {
        return getPinHash(context.applicationContext) != null
    }

    fun getPinHash(
        context: Context
    ): String? {
        return getSharedPreferences(context.applicationContext).getString(
            context.applicationContext.getString(R.string.app_lock_hash_pref), null
        )
    }

    fun setPinHash(
        context: Context, pinHash: String
    ) {
        getSharedPreferences(
            context.applicationContext
        ).edit {
            putString(
                context.applicationContext.getString(R.string.app_lock_hash_pref), pinHash
            )
        }
    }

    fun getSharedPreferences(
        context: Context
    ): SharedPreferences {
        return context.applicationContext.getSharedPreferences(
            context.getString(R.string.app_shared_preferences), Context.MODE_PRIVATE
        )
    }
}