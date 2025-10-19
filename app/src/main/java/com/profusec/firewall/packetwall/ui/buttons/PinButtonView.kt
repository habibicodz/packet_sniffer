package com.profusec.firewall.packetwall.ui.buttons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinButtonView(
    modifier: Modifier = Modifier,
    onPinButtonClicked: ((Int) -> Unit)? = null,
    onClearClicked: (() -> Unit)? = null,
    onOkClicked: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.requiredWidthIn(max = 250.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            PinButton(number = 1, onClicked = onPinButtonClicked)
            PinButton(number = 2, onClicked = onPinButtonClicked)
            PinButton(number = 3, onClicked = onPinButtonClicked)
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            PinButton(number = 4, onClicked = onPinButtonClicked)
            PinButton(number = 5, onClicked = onPinButtonClicked)
            PinButton(number = 6, onClicked = onPinButtonClicked)
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            PinButton(number = 7, onClicked = onPinButtonClicked)
            PinButton(number = 8, onClicked = onPinButtonClicked)
            PinButton(number = 9, onClicked = onPinButtonClicked)
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(icon = Icons.AutoMirrored.Filled.ArrowBack, onClicked = onClearClicked)
            PinButton(number = 0, onClicked = onPinButtonClicked)
            IconButton(icon = Icons.Default.Done, onClicked = onOkClicked)
        }
    }
}


@Composable
private fun PinButton(
    modifier: Modifier = Modifier, number: Int = 0, onClicked: ((Int) -> Unit)? = null
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val isPressed by mutableInteractionSource.collectIsPressedAsState()
    val btnScale by animateFloatAsState(if (isPressed) 0.8f else 1f)

    Box(
        modifier = modifier
            .scale(btnScale)
            .size(60.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .clickable(
                interactionSource = mutableInteractionSource, indication = ripple(), onClick = {
                    onClicked?.invoke(number)
                }), contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(), fontSize = 30.sp, fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onClicked: (() -> Unit)? = null
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val isPressed by mutableInteractionSource.collectIsPressedAsState()
    val btnScale by animateFloatAsState(if (isPressed) 0.8f else 1f)

    Box(
        modifier = modifier
            .scale(btnScale)
            .size(60.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = mutableInteractionSource, indication = ripple(), onClick = {
                    onClicked?.invoke()
                }),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}