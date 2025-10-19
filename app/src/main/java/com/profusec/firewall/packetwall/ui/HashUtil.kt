package com.profusec.firewall.packetwall.ui

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object HashUtil {
    fun toMD5HashString(message: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(message.toByteArray(charset = StandardCharsets.UTF_8))
        return messageDigest.digest().toString(StandardCharsets.UTF_8)
    }
}