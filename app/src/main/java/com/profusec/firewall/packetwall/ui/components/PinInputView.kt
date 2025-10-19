package com.profusec.firewall.packetwall.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun PinInputView(
    modifier: Modifier = Modifier, pin: Array<String> = arrayOf("", "", "", "")
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(pin.size) { index ->
                val char = pin[index]
                PinView(
                    isActive = char.isNotEmpty()
                )
            }
        }
    }
}


@Composable
@Preview
private fun PinView(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    unactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Spacer(
        modifier = modifier
            .size(26.dp)
            .clip(CircleShape)
            .border(width = 2.dp, color = if (isActive) activeColor else unactiveColor, CircleShape)
            .padding(5.dp)
            .background(color = if (isActive) activeColor else unactiveColor, shape = CircleShape)
    )
}