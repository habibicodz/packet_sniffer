package com.android.packetsniffer.ui.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.packetsniffer.R

@Composable
fun ProxyToggleButton(
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    isInProgress: Boolean = false,
    clickable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    // Infinite animation
    val infiniteTransition = rememberInfiniteTransition()

    // Button properties
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val btnScale by animateFloatAsState(if (isPressed || (isInProgress && !isLocked)) 0.8f else 1.0f)
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 1.2f, targetValue = btnScale, animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Lock Open
    val lockOpenBackgroundColor = MaterialTheme.colorScheme.surfaceContainer
    val lockOpenForegroundColor = Color(0xFF757575)

    // Lock colors
    val lockedBackgroundColor = MaterialTheme.colorScheme.primary
    val lockForegroundColor = MaterialTheme.colorScheme.background

    val backgroundColor by animateColorAsState(
        if (isLocked) lockedBackgroundColor else lockOpenBackgroundColor,
        animationSpec = tween(300)
    )

    val foregroundColor by animateColorAsState(
        if (isLocked) lockForegroundColor else lockOpenForegroundColor,
        animationSpec = tween(300)
    )


    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(120.dp),
        ) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = isInProgress,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    color = backgroundColor,
                    strokeWidth = 8.dp,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                )
            }
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(if (!isInProgress && !isLocked && !isPressed) scaleAnim else btnScale)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(color = backgroundColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = { onClick?.invoke() },
                    enabled = clickable
                )
                .padding(20.dp)
        ) {
            if (isLocked) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = foregroundColor
                )
            } else {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.lock_open),
                    contentDescription = null,
                    tint = foregroundColor
                )
            }
        }
    }
}


@Composable
@Preview
fun VpnProxyButtonPreview() {
    ProxyToggleButton(
        isLocked = false,
        onClick = {

        }
    )
}