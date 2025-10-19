package com.profusec.firewall.packetwall.navigation.navhost

import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.profusec.firewall.packetwall.navigation.graph.AppNavGraph
import com.profusec.firewall.packetwall.ui.animations.navEnterTransition
import com.profusec.firewall.packetwall.ui.animations.navExitTransition
import com.profusec.firewall.packetwall.ui.screens.FilterAppsListScreen
import com.profusec.firewall.packetwall.ui.screens.MainScreen
import com.profusec.firewall.packetwall.ui.screens.PinAuthScreen
import com.profusec.firewall.packetwall.util.PreferenceUtil
import com.profusec.firewall.packetwall.viewmodel.MainViewModel


@Composable
fun AppNavHost(
    mViewModel: MainViewModel,
    navController: NavHostController,
    onToggleClicked: (() -> Unit)? = null,
) {
    val context: Context = LocalContext.current
    val applicationContext: Context = context.applicationContext
    val activity = LocalActivity.current
    val proxyServiceState by mViewModel.proxyServiceState.collectAsStateWithLifecycle()
    val connectionInProgress by mViewModel.isConnecting.collectAsStateWithLifecycle()
    val appsFilters by mViewModel.appsFilters.collectAsStateWithLifecycle()
    val appTheme by mViewModel.appTheme.collectAsStateWithLifecycle()
    val startDestination =
        if (PreferenceUtil.isPinSet(LocalContext.current)) AppNavGraph.PinAuth else AppNavGraph.Main


    NavHost(
        navController, startDestination = startDestination, enterTransition = {
        navEnterTransition()
    }, exitTransition = {
        navExitTransition()
    }, popEnterTransition = {
        navEnterTransition()
    },

        popExitTransition = {
            navExitTransition()
        }) {

        composable<AppNavGraph.PinAuth> {
            PinAuthScreen(onBack = {
                activity?.finish()
            }, onAuthenticated = {
                navController.navigate(AppNavGraph.Main) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
        }

        composable<AppNavGraph.Main> {
            MainScreen(
                firewallServiceState = proxyServiceState,
                connectionInProgress = connectionInProgress,
                onBack = {
                    if (PreferenceUtil.isPinSet(applicationContext)) {
                        navController.navigate(AppNavGraph.PinAuth) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    } else {
                        activity?.finishAffinity()
                    }
                },
                onSettingsClicked = {
                    navController.navigate(AppNavGraph.Settings)
                },
                onToggleClicked = onToggleClicked,
                onManageTrackedList = {
                    navController.navigate(
                        AppNavGraph.TrackedAppsList,
                        navOptions = NavOptions.Builder().setRestoreState(true).build()
                    )
                })
        }

        composable<AppNavGraph.Settings> {
            SettingsNavHost(
                mainViewModel = mViewModel, selectedTheme = appTheme, onBack = {
                    navController.popBackStack()
                })
        }

        composable<AppNavGraph.TrackedAppsList> {
            FilterAppsListScreen(appsFilters = appsFilters, onCellularToggle = { packageName ->
                mViewModel.toggleCellularStatus(packageName)
            }, onWifiToggle = { packageName ->
                mViewModel.toggleWifiStatus(packageName)
            }, onBack = {
                navController.popBackStack()
            })
        }
    }
}