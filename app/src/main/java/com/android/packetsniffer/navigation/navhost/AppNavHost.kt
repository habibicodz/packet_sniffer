package com.android.packetsniffer.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.packetsniffer.navigation.graph.AppNavGraph
import com.android.packetsniffer.state.ProxyServiceState
import com.android.packetsniffer.ui.animations.navEnterTransition
import com.android.packetsniffer.ui.animations.navExitTransition
import com.android.packetsniffer.ui.screens.MainScreen
import com.android.packetsniffer.ui.screens.SettingsScreen
import com.android.packetsniffer.ui.screens.TrackedAppsListScreen
import com.android.packetsniffer.viewmodel.MainViewModel


@Composable
fun AppNavHost(
    mViewModel: MainViewModel,
    navController: NavHostController,
    onToggleClicked: (() -> Unit)? = null,
) {
    val proxyServiceState by mViewModel.proxyServiceState.collectAsStateWithLifecycle()
    val connectionInProgress by mViewModel.isConnecting.collectAsStateWithLifecycle()
    val installedAppsList by mViewModel.allInstalledApplications.collectAsStateWithLifecycle()

    NavHost(
        navController,
        startDestination = AppNavGraph.Home,
        enterTransition = {
            navEnterTransition()
        },
        exitTransition = {
            navExitTransition()
        }
    ) {

        composable<AppNavGraph.Home> {
            MainScreen(
                proxyServiceState = proxyServiceState,
                connectionInProgress = connectionInProgress,
                onSettingsClicked = {
                    navController.navigate(AppNavGraph.Settings)
                },
                onToggleClicked = onToggleClicked,
                onManageTrackedList = {
                    navController.navigate(
                        AppNavGraph.TrackedAppsList,
                        navOptions = NavOptions.Builder().setRestoreState(true).build()
                    )
                }
            )
        }

        composable<AppNavGraph.Settings> {
            SettingsScreen {
                navController.popBackStack()
            }
        }

        composable<AppNavGraph.TrackedAppsList> {
            TrackedAppsListScreen (
                list = installedAppsList,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}