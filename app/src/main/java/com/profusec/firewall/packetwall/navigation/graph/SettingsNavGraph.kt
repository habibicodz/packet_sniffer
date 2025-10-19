package com.profusec.firewall.packetwall.navigation.graph

import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsNavGraph(val route: String) {
    @Serializable
    data object Settings : SettingsNavGraph("home_screen")

    @Serializable
    data object PinSet: SettingsNavGraph("pin_set_screen")
}