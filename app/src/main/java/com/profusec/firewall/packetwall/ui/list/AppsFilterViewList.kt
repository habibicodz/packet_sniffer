package com.profusec.firewall.packetwall.ui.list

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.profusec.firewall.packetwall.R
import com.profusec.firewall.packetwall.model.AppFilter
import com.profusec.firewall.packetwall.ui.status.CellularFilterStatus
import com.profusec.firewall.packetwall.ui.status.WifiFilterStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppsFilterViewList(
    modifier: Modifier = Modifier,
    appsFilters: Map<String, AppFilter>,
    onWifiToggle: ((String) -> Unit)? = null,
    onCellularToggle: ((String) -> Unit)? = null
) {
    val appsList by remember(appsFilters) { mutableStateOf(appsFilters.keys.toList()) }

    LazyColumn(
        modifier = modifier.fillMaxWidth(), contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        items(appsFilters.size) { index ->
            val packageName = appsList[index]
            val appFilter = appsFilters[packageName]!!
            AppFilterViewItem(
                modifier = Modifier,
                packageName = packageName,
                appFilter = appFilter,
                onWifiToggle = onWifiToggle,
                onCellularToggle = onCellularToggle
            )
        }
    }
}


@Composable
fun AppFilterViewItem(
    modifier: Modifier = Modifier,
    packageName: String,
    appFilter: AppFilter,
    onWifiToggle: ((String) -> Unit)? = null,
    onCellularToggle: ((String) -> Unit)? = null
) {
    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var iconDrawable by remember { mutableStateOf<Drawable?>(null) }
    var appName by remember { mutableStateOf("") }
    val wifiStatusIcon = painterResource(
        if (appFilter.wifiFilterStatus == WifiFilterStatus.WIFI_ALLOWED) {
            R.drawable.wifi_on
        } else {
            R.drawable.wifi_off
        }
    )
    val wifiIconAlpha = if (appFilter.wifiFilterStatus == WifiFilterStatus.WIFI_ALLOWED) {
        1.0f
    } else {
        0.5f
    }

    val cellularIconAlpha =
        if (appFilter.cellularFilterStatus == CellularFilterStatus.CELLULAR_ALLOWED) {
            1.0f
        } else {
            0.5f
        }

    val cellularStatusIcon =
        painterResource(if (appFilter.cellularFilterStatus == CellularFilterStatus.CELLULAR_ALLOWED) R.drawable.cellular_on else R.drawable.cellular_off)

    DisposableEffect(Unit) {
        val packageManager = context.applicationContext.packageManager

        coroutineScope.launch {
            var name: String? = null
            var drawable: Drawable? = null

            withContext(Dispatchers.IO) {
                try {
                    name = packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                    ).toString()

                } catch (_: Exception) {

                }

                try {
                    drawable =
                        context.applicationContext.packageManager.getApplicationIcon(packageName)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            name?.let {
                appName = name
            }

            drawable?.let {
                iconDrawable = drawable
            }
        }

        onDispose {
            coroutineScope.coroutineContext.cancelChildren()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {

                })
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.size(40.dp)
            ) {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = iconDrawable != null,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center)
                ) {
                    iconDrawable?.let {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            model = ImageRequest.Builder(context).data(it).build(),
                            contentDescription = null
                        )
                    }
                }
            }

            Text(text = appName, color = MaterialTheme.colorScheme.onBackground)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            Icon(
                modifier = Modifier
                    .alpha(wifiIconAlpha)
                    .clip(CircleShape)
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = {
                            onWifiToggle?.invoke(packageName)
                        })
                    .padding(8.dp),
                painter = wifiStatusIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )

            Icon(
                modifier = Modifier
                    .alpha(cellularIconAlpha)
                    .clip(CircleShape)
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = {
                            onCellularToggle?.invoke(packageName)
                        })
                    .padding(8.dp),
                painter = cellularStatusIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
