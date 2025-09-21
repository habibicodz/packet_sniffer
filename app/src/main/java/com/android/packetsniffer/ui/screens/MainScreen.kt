package com.android.packetsniffer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.packetsniffer.R
import com.android.packetsniffer.state.ProxyServiceState
import com.android.packetsniffer.ui.buttons.ProxyToggleButton
import com.android.packetsniffer.ui.list.MiniTrackedAppsList
import com.android.packetsniffer.ui.status.ProxyProtectionStatus
import com.android.packetsniffer.ui.theme.PacketSnifferTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainScreen(
    proxyServiceState: ProxyServiceState = ProxyServiceState.Connected,
    connectionInProgress: Boolean = false,
    onToggleClicked: (() -> Unit)? = null,
    onSettingsClicked: (() -> Unit)? = null,
    onManageTrackedList: (() -> Unit)? = null
) {
    val applicationName = stringResource(R.string.app_name)

    val isLocked = proxyServiceState is ProxyServiceState.Connected
    val statusHighlightColor =
        if (proxyServiceState is ProxyServiceState.Connected) Color(0x1A7AFFAB) else Color(
            0x1AFF7A7A
        )


    PacketSnifferTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    TopAppBar(title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.global_icon),
                                contentDescription = null,
                                tint = if (isLocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.errorContainer
                            )

                            Text(
                                applicationName,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }, actions = {
                        IconButton(
                            onClick = { onSettingsClicked?.invoke() }
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    })
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MiniTrackedAppsList(
                            items = listOf("com.deepforensic.gallerylock"),
                            onClick = {
                                onManageTrackedList?.invoke()
                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProxyToggleButton(
                            isLocked = proxyServiceState is ProxyServiceState.Connected,
                            isInProgress = connectionInProgress,
                            clickable = !connectionInProgress,
                            onClick = onToggleClicked
                        )

                        ProxyProtectionStatus(
                            isProtected = proxyServiceState is ProxyServiceState.Connected
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                statusHighlightColor, Color.Transparent
                            ),
                        ),
                        shape = RectangleShape,
                    )
            )
        }
    }
}