package com.profusec.firewall.packetwall.state

sealed class FirewallServiceState {
    data object Disconnected: FirewallServiceState()
    data object Connected: FirewallServiceState()
}