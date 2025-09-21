package com.android.packetsniffer.navigation.graph

import kotlinx.serialization.Serializable

@Serializable
sealed class AppNavGraph(val route: String) {
    @Serializable
    data object Home : AppNavGraph("home_screen")

    @Serializable
    data object Settings : AppNavGraph("settings_screen")

    @Serializable
    data object TrackedAppsList: AppNavGraph("tracked_apps_list")
}