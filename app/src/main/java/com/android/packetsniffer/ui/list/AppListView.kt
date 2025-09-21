package com.android.packetsniffer.ui.list

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import com.android.packetsniffer.ui.theme.PacketSnifferTheme
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@Composable
fun AppListView(
    modifier: Modifier = Modifier,
    items: List<String>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        items(items.size) { index ->
            AppItemView(
                modifier = Modifier,
                packageName = items[index]
            )
        }
    }
}


@Composable
fun AppItemView(
    modifier: Modifier = Modifier,
    packageName: String,
    onClick: ((String) -> Unit)? = null
) {
    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var iconBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var appName by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val packageManager = context.applicationContext.packageManager

        coroutineScope.launch {
            try {
                appName = packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                ).toString()
            } catch (_: Exception) {

            }

            try {
                val appIconDrawable =
                    context.applicationContext.packageManager.getApplicationIcon(packageName)

                iconBitmap = appIconDrawable.toBitmapOrNull()?.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
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
                    onClick?.invoke(packageName)
                }
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.size(50.dp)
            ) {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = iconBitmap != null,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center)
                ) {
                    iconBitmap?.let {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            bitmap = it, contentDescription = null
                        )
                    }
                }
            }

            Text(text = appName, color = MaterialTheme.colorScheme.onBackground)
        }

        Switch(checked = isEnabled, onCheckedChange = null)
    }
}

@Composable
@Preview
fun AppsListViewPreview() {
    val itemsList = remember { listOf("com.deepforensic.gallerylock") }

    PacketSnifferTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                MiniTrackedAppsList(
                    modifier = Modifier.padding(20.dp),
                    items = itemsList
                )
            }
        }
    }
}