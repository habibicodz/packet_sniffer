package com.profusec.firewall.packetwall.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.profusec.firewall.packetwall.R
import com.profusec.firewall.packetwall.ui.components.MiniAboutView
import com.profusec.firewall.packetwall.ui.dialog.ThemeSelectorDialog
import com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme
import com.profusec.firewall.packetwall.util.AppThemeMode
import com.profusec.firewall.packetwall.util.PreferenceUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: (() -> Unit)? = null,
    selectedTheme: AppThemeMode = AppThemeMode.SYSTEM,
    onPinSetClicked: (() -> Unit)? = null,
    onThemeSelected: ((AppThemeMode) -> Unit)? = null
) {
    val context = LocalContext.current
    val applicationContext: Context = context.applicationContext
    var isThemeDialogShow by remember { mutableStateOf(false) }
    val isPinSet = PreferenceUtil.isPinSet(applicationContext)


    BackHandler {
        onBack?.invoke()
    }

    if (isThemeDialogShow) {
        ThemeSelectorDialog(onSelect = {
            onThemeSelected?.invoke(it)
            isThemeDialogShow = false
        }, onDismissRequest = {
            isThemeDialogShow = false
        })
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = {
                        onBack?.invoke()
                    }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }



                Text(
                    "Settings",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical,
                    )
            ) {
                Spacer(
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
                    text = "General Settings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {
                                isThemeDialogShow = true
                            })
                        .padding(horizontal = 30.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.day_night_icon),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Theme",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                        Text(
                            text = selectedTheme.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {
                                onPinSetClicked?.invoke()
                            })
                        .padding(horizontal = 30.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Pin",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                        Text(
                            text = if (isPinSet) "Modify your PIN" else "Set 4 digit pin",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                MiniAboutView(
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}


@Composable
@Preview
fun SettingsScreenPreview() {
    PacketSnifferTheme {
        SettingsScreen {

        }
    }
}