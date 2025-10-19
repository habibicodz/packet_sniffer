package com.profusec.firewall.packetwall.ui.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.profusec.firewall.packetwall.R

@Composable
fun FirewallProtectionStatus(
    modifier: Modifier = Modifier, isProtected: Boolean = false
) {
    val dotColor =
        if (isProtected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.errorContainer

    val lockStatusColor = if (isProtected) Color(0x807AFFAB) else Color(0x80FF7A7A)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(color = lockStatusColor)
            .padding(10.dp),
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
            text = if (isProtected) stringResource(R.string.connected_firewall_status) else stringResource(
                R.string.disconnected_firewall_status
            ),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
@Preview
fun ProxyProtectionStatusPreview() {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FirewallProtectionStatus(
                modifier = Modifier.align(Alignment.Center), isProtected = true
            )
        }
    }
}