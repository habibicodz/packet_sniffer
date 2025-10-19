package com.profusec.firewall.packetwall.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.profusec.firewall.packetwall.ui.HashUtil
import com.profusec.firewall.packetwall.util.PreferenceUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PinAuthViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val applicationContext: Context = application.applicationContext
    private val _pin = MutableStateFlow(arrayListOf("", "", "", ""))
    val pin = _pin.asStateFlow()

    var onAuthenticated: (() -> Unit)? = null


    private fun getLastWriteIndex(): Int {
        return _pin.value.indexOf("")
    }

    fun removeLastChar() {
        val lastCharTyped = _pin.value.indexOfLast { it.isNotEmpty() }

        if (lastCharTyped > -1) {
            _pin.update {
                ArrayList(
                    it.toMutableList().also { mutableList ->
                        mutableList[lastCharTyped] = ""
                    })
            }
        }
    }

    fun addChar(char: String) {
        viewModelScope.launch {
            val lastIndexToInsert = getLastWriteIndex()
            if (lastIndexToInsert == -1) {
                return@launch
            }

            _pin.update {
                ArrayList(it.toMutableList().also { mutableList ->
                    mutableList[lastIndexToInsert] = char
                })
            }
        }
    }

    fun cleanPin() {
        _pin.update {
            ArrayList(
                it.toMutableList().also { mutableList ->
                    mutableList.replaceAll { "" }
                })
        }
    }

    fun processPin() {
        viewModelScope.launch {
            if (_pin.value.contains("")) {
                return@launch
            }

            val typedPinHash = HashUtil.toMD5HashString(_pin.value.fastJoinToString(""))
            val savedPinHash = PreferenceUtil.getPinHash(applicationContext)

            if (typedPinHash == savedPinHash) {
                onAuthenticated?.invoke()
            } else {
                cleanPin()
            }
        }
    }
}