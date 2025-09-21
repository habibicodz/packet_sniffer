package com.android.packetsniffer.ui.list

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmapOrNull
import com.android.packetsniffer.ui.theme.PacketSnifferTheme
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@Composable
fun MiniTrackedAppsList(
    modifier: Modifier = Modifier,
    items: List<String>,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {
                    onClick?.invoke()
                }
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.alpha(0.7f),
                text = "Tracking list",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp
            )

            Icon(
                modifier = Modifier.alpha(0.7f),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }

        if (items.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(items.size) { index ->
                    MiniTrackedAppItem(items[index])
                }
            }
        }
    }
}

@Composable
fun MiniTrackedAppItem(
    packageName: String
) {
    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var iconBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    DisposableEffect(Unit) {
        coroutineScope.launch {
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

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(5.dp)
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = iconBitmap != null, enter = fadeIn() + expandIn(expandFrom = Alignment.Center)
        ) {
            iconBitmap?.let {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    bitmap = it, contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview
fun MiniTrackedAppsListPreview() {
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