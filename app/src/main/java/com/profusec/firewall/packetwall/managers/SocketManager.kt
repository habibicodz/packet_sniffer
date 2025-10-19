package com.profusec.firewall.packetwall.managers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SocketManager {
    suspend fun requestData (byte: ByteArray) {
        withContext(Dispatchers.IO) {

        }
    }
}