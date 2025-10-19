package com.profusec.firewall.packetwall.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.profusec.firewall.packetwall.ui.HashUtil
import com.profusec.firewall.packetwall.util.PreferenceUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PinChooseViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val  _snackMessage = Channel<String>()
    val snackMessage = _snackMessage.receiveAsFlow()

    private val applicationContext: Context = application.applicationContext
    private val _pin = MutableStateFlow(arrayListOf("", "", "", ""))
    val pin = _pin.asStateFlow()

    private val _selectedPin = MutableStateFlow<String?>(null)
    val selectedPin = _selectedPin.asStateFlow()

    var onPinChoose: (() -> Unit)? = null


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

            val typedPin = _pin.value.fastJoinToString("")
            if (_selectedPin.value != null) {
                if (_selectedPin.value == typedPin) {
                    PreferenceUtil.setPinHash(applicationContext, HashUtil.toMD5HashString(typedPin))
                    _snackMessage.send("PIN created successfully")
                    onPinChoose?.invoke()
                } else {
                    _snackMessage.send("PIN Not Matched")
                    cleanPin()
                    _selectedPin.emit(null)
                }
            } else {
                _selectedPin.emit(typedPin)
                cleanPin()
            }
        }
    }
}