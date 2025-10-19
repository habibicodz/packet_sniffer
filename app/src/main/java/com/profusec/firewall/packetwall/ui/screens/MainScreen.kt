package com.profusec.firewall.packetwall.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.profusec.firewall.packetwall.R
import com.profusec.firewall.packetwall.state.FirewallServiceState
import com.profusec.firewall.packetwall.ui.buttons.FirewallToggleButton
import com.profusec.firewall.packetwall.ui.components.FilterAppView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainScreen(
    firewallServiceState: FirewallServiceState = FirewallServiceState.Connected,
    connectionInProgress: Boolean = false,
    onToggleClicked: (() -> Unit)? = null,
    onSettingsClicked: (() -> Unit)? = null,
    onManageTrackedList: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {

    BackHandler {
        onBack?.invoke()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background, topBar = {
                TopAppBar(title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            modifier = Modifier.size(30.dp).scale(1.5f),
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null
                        )

                        _root_ide_package_.com.profusec.firewall.packetwall.ui.status.FirewallProtectionStatus(
                            isProtected = firewallServiceState is FirewallServiceState.Connected
                        )

                        IconButton(
                            onClick = { onSettingsClicked?.invoke() }) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                })
            }) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = firewallServiceState !is FirewallServiceState.Connected,
                    enter = slideInVertically(animationSpec = tween(200)) { -it } + expandVertically(
                        animationSpec = tween(200)
                    ) { -it },
                    exit = slideOutVertically(animationSpec = tween(200)) { -it } + shrinkVertically(
                        animationSpec = tween(200)
                    ) { -it },
                ) {
                    FilterAppView(
                        modifier = Modifier.padding(20.dp), onClick = onManageTrackedList
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                ) {
                    FirewallToggleButton(
                        modifier = Modifier.align(Alignment.Center),
                        isLocked = firewallServiceState is FirewallServiceState.Connected,
                        isInProgress = connectionInProgress,
                        clickable = !connectionInProgress,
                        onClick = onToggleClicked
                    )
                }
            }
        }
    }
}