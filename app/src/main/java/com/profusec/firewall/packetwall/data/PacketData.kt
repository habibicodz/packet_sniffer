package com.profusec.firewall.packetwall.data

data class PacketData(
    val source: Int, val destination: Int, val payload: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PacketData

        if (source != other.source) return false
        if (destination != other.destination) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source
        result = 31 * result + destination
        result = 31 * result + payload.contentHashCode()
        return result
    }

}