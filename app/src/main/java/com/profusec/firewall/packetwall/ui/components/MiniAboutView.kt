package com.profusec.firewall.packetwall.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme

@Composable
fun MiniAboutView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = Modifier.alpha(0.5f),
            text = "Developed by",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

        Text(
            "habibicodz",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
    }
}


@Composable
@Preview
fun MiniAboutViewPreview() {
    _root_ide_package_.com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme(
        darkTheme = true
    ) {
        MiniAboutView(
            modifier = Modifier.fillMaxWidth()
        )
    }
}