package com.profusec.firewall.packetwall.ui.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.profusec.firewall.packetwall.R

@Composable
fun FirewallToggleButton(
    modifier: Modifier = Modifier,
    isLocked: Boolean = true,
    isInProgress: Boolean = false,
    clickable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    // Button properties
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val btnScale by animateFloatAsState(if (isPressed || isInProgress) 0.8f else 1.0f)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.green_pulse_dot)
    )
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever, speed = 0.5f
    )

    // Lock Open
    val lockOpenBackgroundColor = MaterialTheme.colorScheme.surfaceContainer
    val lockOpenForegroundColor = Color(0xFF757575)

    // Lock colors
    val lockedBackgroundColor = MaterialTheme.colorScheme.primary
    val lockForegroundColor = MaterialTheme.colorScheme.background

    val backgroundColor by animateColorAsState(
        if (isLocked) lockedBackgroundColor else lockOpenBackgroundColor, animationSpec = tween(300)
    )

    val foregroundColor by animateColorAsState(
        if (isLocked) lockForegroundColor else lockOpenForegroundColor, animationSpec = tween(300)
    )


    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(150.dp),
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

        AnimatedVisibility(isLocked && !isInProgress, enter = scaleIn(), exit = fadeOut()) {
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                progress = { progress })
        }

        Box(
            modifier = Modifier
                .size(150.dp)
                .scale(btnScale)
                .clip(CircleShape)
                .background(color = backgroundColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = { onClick?.invoke() },
                    enabled = clickable
                )
                .padding(30.dp)
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
fun ProxyToggleButtonPreview() {
    FirewallToggleButton(
        isLocked = false, onClick = {

        })
}