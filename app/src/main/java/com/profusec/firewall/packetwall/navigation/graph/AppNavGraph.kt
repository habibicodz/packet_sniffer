package com.profusec.firewall.packetwall.navigation.graph

import kotlinx.serialization.Serializable

@Serializable
sealed class AppNavGraph(val route: String) {

    @Serializable
    data object PinAuth : AppNavGraph("pin_auth")

    @Serializable
    data object Main : AppNavGraph("main_screen")

    @Serializable
    data object Settings : AppNavGraph("settings_screen")

    @Serializable
    data object TrackedAppsList : AppNavGraph("tracked_apps_list")
}