package com.android.packetsniffer.ui.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.packetsniffer.R
import com.android.packetsniffer.ui.theme.PacketSnifferTheme

@Composable
fun ProxyProtectionStatus(
    modifier: Modifier = Modifier,
    isProtected: Boolean = false
) {
    val dotColor =
        if (isProtected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.errorContainer

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color = dotColor)
        )

        Text(
            text = if (isProtected) stringResource(R.string.connected) else stringResource(R.string.disconnected),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
@Preview
fun ProxyProtectionStatusPreview() {
    PacketSnifferTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                ProxyProtectionStatus(
                    modifier = Modifier.align(Alignment.Center),
                    isProtected = true
                )
            }
        }
    }
}