package com.profusec.firewall.packetwall.socket

object Ipv4Util {
    @JvmStatic
    fun extractSourceIpFromIpv4(packet: ByteArray): ByteArray {
        return packet.copyOfRange(12, 16)
    }

    @JvmStatic
    fun extractDestinationFromIpv4(packet: ByteArray): ByteArray {
        return packet.copyOfRange(16, 20)
    }
}