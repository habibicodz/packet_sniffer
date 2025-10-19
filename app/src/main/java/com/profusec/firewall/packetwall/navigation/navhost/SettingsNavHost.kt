package com.profusec.firewall.packetwall.navigation.navhost

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.profusec.firewall.packetwall.navigation.graph.SettingsNavGraph
import com.profusec.firewall.packetwall.ui.animations.navEnterTransition
import com.profusec.firewall.packetwall.ui.animations.navExitTransition
import com.profusec.firewall.packetwall.ui.screens.PinChooseScreen
import com.profusec.firewall.packetwall.ui.screens.SettingsScreen
import com.profusec.firewall.packetwall.util.AppThemeMode
import com.profusec.firewall.packetwall.viewmodel.MainViewModel

@Composable
fun SettingsNavHost(
    mainViewModel: MainViewModel, selectedTheme: AppThemeMode, onBack: () -> Boolean
) {
    val navController = rememberNavController()

    BackHandler {
        onBack.invoke()
    }

    NavHost(
        navController = navController,
        startDestination = SettingsNavGraph.Settings,
        enterTransition = {
            navEnterTransition()
        },
        exitTransition = {
            navExitTransition()
        },
        popEnterTransition = {
            navEnterTransition()
        }) {
        composable<SettingsNavGraph.Settings> {
            SettingsScreen(selectedTheme = selectedTheme, onPinSetClicked = {
                navController.navigate(SettingsNavGraph.PinSet)
            }, onThemeSelected = {
                mainViewModel.setTheme(it)
            }, onBack = {
                onBack.invoke()
            })
        }

        composable<SettingsNavGraph.PinSet> {
            PinChooseScreen(modifier = Modifier, onBack = {
                navController.popBackStack()
            }, onPinChoose = {
                navController.popBackStack()
            })
        }
    }
}